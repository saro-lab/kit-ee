package me.saro.kit.crypt

import me.saro.kit.fn.SecurityKit
import java.io.InputStream
import java.io.OutputStream
import javax.crypto.Cipher

class Decrypt internal constructor(private val cipher: Cipher) {
    fun decrypt(data: ByteArray): ByteArray = cipher.doFinal(data)
    fun decryptBase64ToByte(data: String): ByteArray = cipher.doFinal(SecurityKit.deBase64(data))
    fun decryptBase64ToString(data: String): String = String(cipher.doFinal(SecurityKit.deBase64(data)), Charsets.UTF_8)
    fun decryptBase64UrlToByte(data: String): ByteArray = cipher.doFinal(SecurityKit.deBase64Url(data))
    fun decryptBase64UrlToString(data: String): String = String(cipher.doFinal(SecurityKit.deBase64Url(data)), Charsets.UTF_8)
    fun decryptByteStream(inputStream: InputStream, outputStream: OutputStream) = SecurityKit.link(cipher, inputStream, outputStream)
}
