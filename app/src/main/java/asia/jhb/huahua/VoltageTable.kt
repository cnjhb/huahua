package asia.jhb.huahua

class VoltageTable :Iterable<VoltageItem>{
    private val voltageItems= HashSet<VoltageItem>()

    fun size()=voltageItems.size
    fun get(i:Int)=
        voltageItems.toArray()[i] as VoltageItem
    fun add(item: VoltageItem)=
        voltageItems.add(item)
    fun remove(item:VoltageItem)=
        voltageItems.remove(item)
    fun getIndex(item:VoltageItem):Int{
        val items=voltageItems.toArray()
        for(i in items.indices){
            if(items[i]==item){
                return i
            }
        }
        return -1
    }

    override fun iterator(): Iterator<VoltageItem> {
        return voltageItems.iterator()
    }
}