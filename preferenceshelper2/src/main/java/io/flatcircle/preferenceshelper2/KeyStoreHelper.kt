package io.flatcircle.preferenceshelper2

import android.content.Context
import android.security.KeyPairGeneratorSpec
import android.util.Base64
import android.util.Log
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.security.auth.x500.X500Principal

object KeyStoreHelper {

    fun encryptString(keyStore: KeyStore, keyStoreAlias: String, inputValue: String): String {
        require(inputValue.isNotEmpty())

        try {
            keyStore.load(null)

            val publicKey = keyStore.getCertificate(keyStoreAlias).publicKey

            val input = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL")
            input.init(Cipher.ENCRYPT_MODE, publicKey)

            val outputStream = ByteArrayOutputStream()
            val cipherOutputStream = CipherOutputStream(
                outputStream, input)
            cipherOutputStream.write(inputValue.toByteArray(charset("UTF-8")))
            cipherOutputStream.close()

            val vals = outputStream.toByteArray()
            return Base64.encodeToString(vals, Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e("KeyStoreHelper", "$e")
        }
        return ""
    }


    fun decryptString(keyStore: KeyStore, keyStoreAlias: String, encryptedText: String): String {
        if (encryptedText.isEmpty())
            return ""

        if (encryptedText.isNotEmpty()) {
            try {
                val privateKey = keyStore.getKey(keyStoreAlias, null) as PrivateKey

                val output = Cipher.getInstance("RSA/ECB/PKCS1Padding")
                output.init(Cipher.DECRYPT_MODE, privateKey)

                val cipherInputStream = CipherInputStream(
                    ByteArrayInputStream(Base64.decode(encryptedText, Base64.DEFAULT)), output)

                val valuesByteArray = cipherInputStream.readBytes()

                return String(valuesByteArray, 0, valuesByteArray.size, Charsets.UTF_8)
            } catch (e: Exception) {
                Log.e("KeyStoreHelper", "$e")
            }
        } else {
            Log.w("KeyStoreHelper", "decryptString() : encryptedText is null or empty.")
        }

        return ""
    }


    @Throws(java.lang.IllegalStateException::class)
    fun getKeyStore(context: Context, keyStoreProvider: String, keyStoreAlias: String): KeyStore {
        val keyStore = KeyStore.getInstance(keyStoreProvider)
        keyStore.load(null)
        createNewKeyIfNecessary(context, keyStore, keyStoreProvider, keyStoreAlias)
        return keyStore
    }


    private fun createNewKeyIfNecessary(context: Context, keyStore: KeyStore, keyStoreProvider: String, keyStoreAlias: String) {

        if (keyStoreAlias.isEmpty()) {
            throw IllegalStateException("You need to set the keyStoreAlias when using encryption")
        }

        try {
            // Create new key if needed
            if (!keyStore.containsAlias(keyStoreAlias)) {
                val start = Calendar.getInstance()
                val end = Calendar.getInstance()
                end.add(Calendar.YEAR, 1)
                val spec = KeyPairGeneratorSpec.Builder(context)
                    .setAlias(keyStoreAlias)
                    .setSubject(X500Principal("CN=Sample Name, O=Android Authority"))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(start.time)
                    .setEndDate(end.time)
                    .build()
                val generator = KeyPairGenerator.getInstance("RSA", keyStoreProvider)
                generator.initialize(spec)

                generator.generateKeyPair()
            }

        } catch (e: Exception) {
            Log.e("KeyStoreHelper", "$e")
        }
    }
}