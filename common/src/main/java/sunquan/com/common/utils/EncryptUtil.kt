package sunquan.com.common.utils

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2019/12/19
 **/
object EncryptUtil {
    fun genRandomUUID(): String {
        return UUID.randomUUID().toString()
    }

    fun md5(string: String): String {
        val var1: Any? = null

        val hash: ByteArray
        try {
            hash = string.toByteArray(charset("UTF-8"))
        } catch (var3: UnsupportedEncodingException) {
            throw RuntimeException("Huh,UTF-8 should be supported?", var3)
        }

        return computeMD5(hash) ?: ""
    }

    private fun computeMD5(input: ByteArray): String? {
        try {
            if (null == input) {
                return null
            } else {
                val md: MessageDigest = MessageDigest.getInstance("MD5")
                md.update(input, 0, input.size)
                val md5bytes: ByteArray = md.digest()
                val hexString = StringBuffer()

                for (i in md5bytes.indices) {
                    val hex = Integer.toHexString(255 and md5bytes[i].toInt())
                    if (hex.length == 1) {
                        hexString.append('0')
                    }

                    hexString.append(hex)
                }

                return hexString.toString()
            }
        } catch (var6: NoSuchAlgorithmException) {
            throw RuntimeException(var6)
        }

    }
}