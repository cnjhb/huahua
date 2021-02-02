package asia.jhb.huahua

object CurrentDevice{
    var device:Device? = null
    fun setVotageItem(item: VoltageItem)= device?.setVotageItem(item)
    fun getVotageTable()=device?.voltageTable
    fun setBoot()= device?.setBoot()
}