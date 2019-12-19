package sunquan.com.common.utils
/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2019/12/19
 **/
object Check {
    fun isEmpty(str: CharSequence): Boolean {
        return isNull(str) || str.length == 0
    }

    fun isNotEmpty(str: CharSequence): Boolean {
        return !isEmpty(str)
    }

    fun isEmpty(l: Collection<*>): Boolean {
        return isNull(l) || l.isEmpty()
    }

    fun isEmpty(os: Array<Any>): Boolean {
        return isNull(os) || os.size == 0
    }

    fun isEmpty(m: Map<*, *>): Boolean {
        return isNull(m) || m.isEmpty()
    }

    fun isNull(o: Any?): Boolean {
        return o == null
    }

    fun isipv4(ipv4: String?): Boolean {
        if (ipv4 == null || ipv4.length == 0) {
            return false//字符串为空或者空串
        }
        val parts = ipv4.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (parts.size != 4) {
            return false
        }
        for (i in parts.indices) {
            try {
                val n = Integer.parseInt(parts[i])
                if (n < 0 || n > 255) {
                    return false
                }
            } catch (e: NumberFormatException) {
                return false
            }

        }
        return true
    }

    fun validUrl(url: String?): Boolean {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"))
    }


}