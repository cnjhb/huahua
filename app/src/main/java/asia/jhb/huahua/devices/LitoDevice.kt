package asia.jhb.huahua.devices

import asia.jhb.huahua.*
import asia.jhb.huahua.utils.TextFile
import com.topjohnwu.superuser.Shell
import java.io.File
import java.io.FileWriter
import java.util.regex.Pattern
import kotlin.text.StringBuilder

class LitoDevice(override val bootPath: String="/dev/block/by-name/boot") : Device() {
    private val directory=Shell.su("pwd").exec().out[0]
    private val target=File("$directory/dts")
    private val dtschanged=File("$directory/dtschanged")
    override val voltageTable= VoltageTable()
    private val bins=ArrayList<Bin>()
    private val device="'compatible = \"qcom,lito\"'"
    init {
        getBoot()
        unpackBoot()
        Shell.su("dtp -i dtb").exec()
        if(checkDevice())
        {
            var dts=TextFile.read(target)
            val p= Pattern.compile("opp-(\\d+)\\s*\\{\\s*opp-hz\\s*=\\s*<\\w+\\s+\\w+>;\\s*opp-microvolt\\s*=\\s*<(\\w+)>;\\s*\\};",
                Pattern.DOTALL)
            val m=p.matcher(dts)
            while(m.find()){
                voltageTable.add(VoltageItem(m.group(1).toInt(),when(Integer.valueOf(m.group(2).replace("0x",""),16)){
                    VoltageLevel.MIN_SYS.value->VoltageLevel.MIN_SYS
                    VoltageLevel.LOW_SYS.value->VoltageLevel.LOW_SYS
                    VoltageLevel.NOM.value->VoltageLevel.NOM
                    VoltageLevel.NOM_L1.value->VoltageLevel.NOM_L1
                    VoltageLevel.NOM_L2.value->VoltageLevel.NOM_L2
                    VoltageLevel.SYS.value->VoltageLevel.SYS
                    VoltageLevel.TURBO.value->VoltageLevel.TURBO
                    VoltageLevel.TURBO_L1.value->VoltageLevel.TURBO_L1
                    VoltageLevel.SYS_L1.value->VoltageLevel.SYS_L1
                    else -> VoltageLevel.ERRO
                }))
            }
            dts=dts.replace(Regex("opp-(\\d+)\\s*\\{\\s*opp-hz\\s*=\\s*<\\w+\\s+\\w+>;\\s*opp-microvolt\\s*=\\s*<(\\w+)>;\\s*\\};"),"")
            val fw=FileWriter(dtschanged)
            fw.write(dts.toString())
            fw.flush()
            fw.close()
        }
    }
    override fun checkDevice(): Boolean {
        if(target.exists())
            return true
        for(file in File(directory).listFiles { dir, name -> name.matches(Regex("dtb-\\d+"))})
        {
            Shell.su("dtc -I dtb -O dts $file -o dts").exec()
            if(Shell.su("cat dts|grep $device").exec().code==0)
                break
            Shell.su("rm -rf dts").exec()
        }
        return target.exists()
    }

    override fun setVotageItem(item: VoltageItem):Boolean {
        if(voltageTable.add(item)){
            voltageTable.remove(item)
            return false
        }
        voltageTable.remove(item)
        voltageTable.add(item)
        return true
    }
    override fun addVoltageItem(item: VoltageItem)=voltageTable.add(item)
    override fun setBoot(): Shell.Result {
        var dts=TextFile.read(dtschanged)
        val position=dts.lastIndexOf("phandle = <0x14c>;")+"phandle = <0x14c>;".length
        val new_items=ArrayList<VoltageItem>()
        for(item in voltageTable){
            val regex=Regex("opp-${item.frequency}\\s*\\{\\s*opp-hz\\s*=\\s*<\\w+\\s+\\w+>;\\s*opp-microvolt\\s*=\\s*<(\\w+)>;\\s*\\};")
            if(regex.containsMatchIn(dts))
                dts=dts.replace(regex,
                    "opp-${item.frequency} { opp-hz = <0x0 0x${Integer.toHexString(item.frequency)}>; opp-microvolt = <0x${Integer.toHexString(item.voltage.value)}>; };")
            else{
                new_items.add(item)
            }

        }
        val tmp=StringBuilder(dts)
        for(item in new_items){
            tmp.insert(position," opp-${item.frequency} { opp-hz = <0x0 0x${Integer.toHexString(item.frequency)}>; opp-microvolt = <0x${Integer.toHexString(item.voltage.value)}>; };")
        }
        Shell.su("chmod 0777 dtschanged").exec()
        Shell.su("chmod 0777 dtb").exec()
        val fw=FileWriter(dtschanged)
        fw.write(tmp.toString())
        fw.flush()
        fw.close()
        dts2dtb()
        repackBoot()
        return super.setBoot()
    }

    override fun dts2dtb()=Shell.su("dtc -I dts -O dtb dtschanged -o dtb").exec()
    private inner class Bin(val id:Int) {
        val frequencyItems=ArrayList<FrequencyItem>()
    }
}