package me.saro.kit.fn

import java.io.InputStream
import java.io.OutputStream
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher


class SecurityKit {
    companion object {
        val EN_BASE64 = Base64.getEncoder()
        val DE_BASE64 = Base64.getDecoder()
        val EN_BASE64_URL = Base64.getUrlEncoder()
        val DE_BASE64_URL = Base64.getUrlDecoder()
        val HEXS: CharArray = (0..255).joinToString("") { String.format("%02x", it) }.toCharArray()

        @JvmStatic
        fun md5(data: ByteArray): ByteArray = hash("MD5", data)
        @JvmStatic
        fun md5Hex(data: ByteArray): String = hashHex("MD5", data)
        @JvmStatic
        fun md5Hex(data: String): String = hashHex("MD5", data.toByteArray(Charsets.UTF_8))

        @JvmStatic
        fun sha1(data: ByteArray): ByteArray = hash("SHA-1", data)
        @JvmStatic
        fun sha1Hex(data: ByteArray): String = hashHex("SHA-1", data)
        @JvmStatic
        fun sha1Hex(data: String): String = hashHex("SHA-1", data.toByteArray(Charsets.UTF_8))

        @JvmStatic
        fun sha256(data: ByteArray): ByteArray = hash("SHA-256", data)
        @JvmStatic
        fun sha256Hex(data: ByteArray): String = hashHex("SHA-256", data)
        @JvmStatic
        fun sha256Hex(data: String): String = hashHex("SHA-256", data.toByteArray(Charsets.UTF_8))

        @JvmStatic
        fun sha512(data: ByteArray): ByteArray = hash("SHA-512", data)
        @JvmStatic
        fun sha512Hex(data: ByteArray): String = hashHex("SHA-512", data)
        @JvmStatic
        fun sha512Hex(data: String): String = hashHex("SHA-512", data.toByteArray(Charsets.UTF_8))

        @JvmStatic
        fun sha3x256(data: ByteArray): ByteArray = hash("SHA3-256", data)
        @JvmStatic
        fun sha3x256Hex(data: ByteArray): String = hashHex("SHA3-256", data)
        @JvmStatic
        fun sha3x256Hex(data: String): String = hashHex("SHA3-256", data.toByteArray(Charsets.UTF_8))

        @JvmStatic
        fun sha3x512(data: ByteArray): ByteArray = hash("SHA3-512", data)
        @JvmStatic
        fun sha3x512Hex(data: ByteArray): String = hashHex("SHA3-512", data)
        @JvmStatic
        fun sha3x512Hex(data: String): String = hashHex("SHA3-512", data.toByteArray(Charsets.UTF_8))

        @JvmStatic
        fun hash(algorithm: String, data: ByteArray): ByteArray = MessageDigest.getInstance(algorithm).digest(data)

        @JvmStatic
        fun hashHex(algorithm: String, data: ByteArray): String = hex(hash(algorithm, data))

        @JvmStatic
        fun hex(bytes: ByteArray): String {
            val rv = CharArray(bytes.size * 2)
            for (i in bytes.indices) {
                System.arraycopy(HEXS, (bytes[i].toInt() and 0xff) * 2, rv, i*2, 2)
            }
            return String(rv)
        }

        @JvmStatic
        fun enBase64(data: ByteArray): String = EN_BASE64.encodeToString(data)
        @JvmStatic
        fun deBase64(data: String): ByteArray = DE_BASE64.decode(data)
        @JvmStatic
        fun enBase64Url(data: ByteArray): String = EN_BASE64_URL.encodeToString(data)
        @JvmStatic
        fun deBase64Url(data: String): ByteArray = DE_BASE64_URL.decode(data)

        @JvmStatic
        fun link(cipher: Cipher, inputStream: InputStream, outputStream: OutputStream) {
            val buffer = ByteArray(8192)
            var len: Int
            while (inputStream.read(buffer).also { len = it } > 0) {
                outputStream.write(cipher.update(buffer, 0, len))
            }
            outputStream.write(cipher.doFinal())
        }
    }
}
