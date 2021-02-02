package asia.jhb.huahua

import android.content.Context
import com.topjohnwu.superuser.Shell

abstract class Device() {
    abstract fun checkDevice():Boolean
    abstract fun setVotageItem(item:VoltageItem):Boolean
    abstract fun addVoltageItem(item: VoltageItem):Boolean
    abstract val bootPath:String
    abstract val voltageTable:VoltageTable
    open fun setBoot()=Shell.su("dd if=new-boot.img of=$bootPath").exec()
    fun getBoot()=Shell.su("dd if=$bootPath of=boot.img").exec()
    fun unpackBoot()=Shell.su("magiskboot unpack boot.img").exec()
    fun repackBoot()=Shell.su("magiskboot repack boot.img").exec()
    open fun dts2dtb()=Shell.su("dtc -O dtb -I dts dts -o dtb").exec()
}