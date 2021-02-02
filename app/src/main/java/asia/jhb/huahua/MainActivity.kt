package asia.jhb.huahua

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import asia.jhb.huahua.devices.LitoDevice
import asia.jhb.huahua.utils.TextFile
import com.topjohnwu.superuser.Shell
import java.io.*
import java.lang.NumberFormatException
import java.lang.StringBuilder
import java.util.regex.Pattern
import kotlin.concurrent.thread
import dalvik.system.DexFile
import dalvik.system.PathClassLoader
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList


class MainActivity(override var fragment:Fragment=VoltageTableFragment()) : SingleFragmentActivity() {
    val TAG="MainActivity"
    fun getClassName(packageName: String?): List<String>? {
        val classNameList: MutableList<String> = ArrayList()
        try {
            val df = DexFile(this.packageCodePath) //通过DexFile查找当前的APK中可执行文件
            val enumeration = df.entries() //获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            while (enumeration.hasMoreElements()) { //遍历
                val className = enumeration.nextElement() as String
                if (className.contains(packageName!!)) { //在当前所有可执行的类里面查找包含有该包名的所有类
                    classNameList.add(className)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return classNameList
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        if(CurrentDevice.device!=null) {
            super.onCreate(savedInstanceState)
            return
        }
        val pointDialog = AlertDialog.Builder(this).setTitle(android.R.string.dialog_alert_title)
            .setPositiveButton(android.R.string.ok) { dialogInterface: DialogInterface, i: Int -> finish() }
        if (!Shell.rootAccess()) {
            pointDialog.setMessage(R.string.request_root).show()
            Looper.loop()
        }
        copyAssetAndWrite("dtc")
        copyAssetAndWrite("magiskboot")
        copyAssetAndWrite("dtp")
        Shell.su("cd $cacheDir").exec()
        Shell.su("export PATH=$filesDir/:\$PATH").exec()
        Shell.su("chmod 0755 $filesDir/*").exec()
        var device:Device
        for(classname in this.getClassName("asia.jhb.huahua.device")!!)
        {
            try{
                device=classLoader.loadClass(classname).newInstance() as Device
                if(device.checkDevice())
                {
                    CurrentDevice.device=device
                    break
                }
            }catch (igore:IllegalAccessException){}
        }
        if(CurrentDevice.device==null){
            pointDialog.setMessage(R.string.incompatible_device).show()
            Looper.loop()
        }
        super.onCreate(savedInstanceState)
    }
    private fun copyAssetAndWrite(fileName: String): Boolean {
        try {
            if (!filesDir.exists())
                filesDir.mkdirs()
            val outFile = File(filesDir, fileName)
            if (!outFile.exists())
                if (!outFile.createNewFile())
                    return false
                else
                    if (outFile.length() > 10)
                        return true
            val iS = assets.open(fileName)
            val fos = FileOutputStream(outFile)
            val buffer = ByteArray(1024)
            var byteCount: Int = 0
            do {
                byteCount = iS.read(buffer)
                if (byteCount != -1)
                    fos.write(buffer, 0, byteCount)
                else
                    break
            } while (true)
            fos.flush()
            iS.close()
            fos.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }
}