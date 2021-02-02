package asia.jhb.huahua

data class FrequencyItem(var freq:Long,var bus_freq:Long,var bus_min:Long,var acd_leve:Long){
    override fun equals(other: Any?)=other is FrequencyItem&&other.freq==this.freq
    override fun hashCode()=(freq*31) as Int
}
