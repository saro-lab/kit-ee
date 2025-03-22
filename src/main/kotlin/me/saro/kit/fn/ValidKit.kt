package me.saro.kit.fn


/**
 * valid util
 * @author PARK Yong Seo
 * @since 1.0.0
 */
open class ValidKit {
    companion object {
        val IS_HOST_NAME_REGEX = Regex("[_a-z0-9\\-]+")

        @JvmStatic
        fun isEmail(email: String?, length: Int): Boolean =
            isEmail(email, length, emptyList())

        @JvmStatic
        fun isEmail(email: String?, length: Int, options: List<ValidEmail>): Boolean {
            val idAndDomain = email?.split("@")
                ?: return false
            if (idAndDomain.size != 2) {
                return false
            }
            val ids = idAndDomain[0].split(".")
            val domains = idAndDomain[1].split(".")
            if (ids.any { !it.matches(IS_HOST_NAME_REGEX) }) {
                return false
            }
            if (domains.any { !it.matches(IS_HOST_NAME_REGEX) }) {
                return false
            }
            if ((domains.size == 1) && ValidEmail.WITH_TOP_LEVEL_DOMAIN !in options) {
                return false
            }
            return email.length <= length
        }

        /**
         * all parameters is not null
         * @param objs
         * @return
         */
        @JvmStatic
        fun isNotNull(vararg objs: Any?): Boolean {
            if (objs.isEmpty()) {
                return false
            }
            for (obj in objs) {
                if (obj == null) {
                    return false
                }
            }
            return true
        }

        /**
         * all parameters is not blank
         * @param texts
         * @return
         */
        @JvmStatic
        fun isNotBlank(vararg texts: String?): Boolean {
            if (texts.isEmpty()) {
                return false
            }
            for (text in texts) {
                if (text.isNullOrBlank()) {
                    return false
                }
            }
            return true
        }


    }
}