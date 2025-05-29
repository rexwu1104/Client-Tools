package clienttools.configs

abstract class Config {
    abstract fun getIdentity(): String

    override fun toString(): String {
        return "${this::class.java.name}<hash=${getIdentity()}>"
    }
}