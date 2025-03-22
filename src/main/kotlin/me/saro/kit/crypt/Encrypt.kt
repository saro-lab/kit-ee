package me.saro.kit.crypt

import me.saro.kit.fn.SecurityKit
import java.io.InputStream
import java.io.OutputStream
import javax.crypto.Cipher

class Encrypt internal constructor(private val cipher: Cipher) {
    fun encrypt(data: ByteArray): ByteArray = cipher.doFinal(data)
    fun encrypt(data: String): ByteArray = cipher.doFinal(data.toByteArray())
    fun encryptBase64(data: ByteArray): String = SecurityKit.enBase64(cipher.doFinal(data))
    fun encryptBase64(data: String): String = SecurityKit.enBase64(cipher.doFinal(data.toByteArray()))
    fun encryptBase64Url(data: ByteArray): String = SecurityKit.enBase64Url(cipher.doFinal(data))
    fun encryptBase64Url(data: String): String = SecurityKit.enBase64Url(cipher.doFinal(data.toByteArray()))
    fun encryptByteStream(inputStream: InputStream, outputStream: OutputStream) = SecurityKit.link(cipher, inputStream, outputStream)
}
