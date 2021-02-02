package asia.jhb.huahua
data class VoltageItem(var frequency:Int=0,var voltage:VoltageLevel=VoltageLevel.ERRO){
    override fun equals(other: Any?)=
        other is VoltageItem&&other.frequency==this.frequency
    override fun hashCode()=this.frequency*31
}