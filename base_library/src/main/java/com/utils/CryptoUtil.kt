package com.utils

import android.util.Base64

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.io.*
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.PublicKey
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException

/**
 * Created by yangsk on 2017/7/4.
 */

class CryptoUtil(key: String) {

    private var mCipher: Cipher? = null
    private var mKeySpec: SecretKeySpec? = null

    init {
        try {
            mKeySpec = SecretKeySpec(generateKey(key), "AES")
        } catch (e: Exception) {
            e.message?.let { Logger.e(javaClass.simpleName, it) }
        }

    }

    private fun setupEnCrypto() {
        val iv =
            byteArrayOf(0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f)
        val paramSpec = IvParameterSpec(iv)
        try {
            mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            mCipher?.init(Cipher.ENCRYPT_MODE, mKeySpec, paramSpec)
        } catch (e: Exception) {
            mCipher = null
            e.message?.let { Logger.e(javaClass.simpleName, it) }
        }

    }

    private fun setupDeCrypto() {
        val iv =
            byteArrayOf(0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f)
        val paramSpec = IvParameterSpec(iv)
        try {
            mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            mCipher?.init(Cipher.DECRYPT_MODE, mKeySpec, paramSpec)
        } catch (e: Exception) {
            mCipher = null
            e.message?.let { Logger.e(javaClass.simpleName, it) }
        }

    }

    fun encrypt(plaintext: String): String {
        setupEnCrypto()
        try {
            val ciphertext = mCipher?.doFinal(plaintext.toByteArray(charset("UTF-8")))
            return Base64.encodeToString(ciphertext, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.message?.let { Logger.e(javaClass.simpleName, it) }
            return ""
        }

    }

    fun decrypt(plaintext: String): String {
        setupDeCrypto()
        try {
            val ciphertext: ByteArray? = mCipher?.doFinal(Base64.decode(plaintext, Base64.NO_WRAP))
            return ciphertext?.let { String(it, StandardCharsets.UTF_8) }.toString()
        } catch (e: Exception) {
            e.message?.let { Logger.e(javaClass.simpleName, it) }
            return ""
        }

    }

    @Throws(IOException::class)
    private fun readKeyFromStream(keyStream: InputStream): PublicKey? {
        val oin = ObjectInputStream(BufferedInputStream(keyStream))
        try {
            return oin.readObject() as PublicKey
        } catch (e: Exception) {
            return null
        } finally {
            oin.close()
        }
    }

    fun rsaEncrypt(keyStream: InputStream, data: String): String {
        try {
            return rsaEncrypt(keyStream, data.toByteArray(charset("UTF-8")))
        } catch (e: UnsupportedEncodingException) {
            return ""
        }

    }

    fun rsaEncrypt(keyStream: InputStream, data: ByteArray): String {
        try {
            val pubKey = readKeyFromStream(keyStream)
            val cipher = Cipher.getInstance("RSA/ECB/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, pubKey)
            val cipherData = cipher.doFinal(data)
            return Base64.encodeToString(cipherData, Base64.NO_WRAP)
        } catch (e: Exception) {
            return ""
        }

    }

    companion object {

        fun md5(plain: String): String {
            try {
                val m = MessageDigest.getInstance("MD5")
                m.update(plain.toByteArray())
                val digest = m.digest()
                val bigInt = BigInteger(1, digest)
                var hashtext = bigInt.toString(16)
                while (hashtext.length < 32) {
                    hashtext = "0$hashtext"
                }
                return hashtext
            } catch (e: Exception) {
                return ""
            }

        }

        private fun generateKey(input: String): ByteArray? {
            try {
                val bytesOfMessage = input.toByteArray(charset("UTF-8"))
                val md = MessageDigest.getInstance("SHA256")
                return md.digest(bytesOfMessage)
            } catch (e: Exception) {
                return null
            }

        }
    }

}