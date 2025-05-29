package clienttools.utils

object StringUtils {
    fun keyBinding(modId: String, name: String): String {
        return "key.$modId.$name"
    }

    fun keyCategory(modId: String, name: String): String {
        return "category.$modId.key"
    }
}