package asia.jhb.huahua

enum class VoltageLevel(val value:Int) {
    ERRO(0),
    MIN_SYS(48),
    LOW_SYS(64),
    SYS(128),
    SYS_L1(192),
    NOM(256),
    NOM_L1(320),
    NOM_L2(336),
    TURBO(384),
    TURBO_L1(416)
}