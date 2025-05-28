package clienttools.utils

import java.security.MessageDigest

object HashUtils {
    @OptIn(ExperimentalStdlibApi::class)
    fun sha256Hash(string: String): String {
        val bytes = string.byteInputStream().readAllBytes()
        val digest = MessageDigest.getInstance("SHA-256")
        val sha256 = digest.digest(bytes)
        return sha256.toHexString()
    }
}