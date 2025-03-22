package me.saro.kit.crypt

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Crypt private constructor(
    private val transformation: String,
) {
    private var key = ByteArray(0)
    private var iv = ByteArray(0)

    companion object {
        @JvmStatic
        fun create(transformation: String): Crypt {
            if (transformation.split('/').size != 3) {
                throw IllegalArgumentException("transformation must be 3 parts. ex) AES/CBC/PKCS5Padding")
            }
            return Crypt(transformation)
        }
    }

    val algorithm get() = transformation.split("/")[0]
    val mode get() = transformation.split("/")[1]
    val padding get() = transformation.split("/")[2]

    fun key(key: String): Crypt {
        this.key = key.toByteArray()
        return this
    }

    fun key(key: ByteArray): Crypt {
        this.key = key
        return this
    }

    fun iv(iv: String): Crypt {
        this.iv = iv.toByteArray()
        return this
    }

    fun iv(iv: ByteArray): Crypt {
        this.iv = iv
        return this
    }

    fun encrypt(): Encrypt = Encrypt(Cipher.getInstance(transformation).also {
        if (iv.isNotEmpty()) {
            it.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, algorithm), IvParameterSpec(iv))
        } else {
            it.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, algorithm))
        }
    })

    fun decrypt(): Decrypt = Decrypt(Cipher.getInstance(transformation).also {
        if (iv.isNotEmpty()) {
            it.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, algorithm), IvParameterSpec(iv))
        } else {
            it.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, algorithm))
        }
    })
}
