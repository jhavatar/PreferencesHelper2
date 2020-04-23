package io.flatcircle.preferenceshelper2

import android.content.Context
import java.security.KeyStore

class EncryptStringPrefsBackend(private val keyStoreAlias: String, context: Context): BasicPrefsBackend(context) {

    companion object {
        private const val KEY_STORE_PROVIDER = "AndroidKeyStore"
    }


    init {
        check(keyStoreAlias.isNotEmpty()) { "keyStoreAlias should be non-null" }
    }


    private val keyStore : KeyStore by lazy {
        KeyStoreHelper.getKeyStore(context, KEY_STORE_PROVIDER, keyStoreAlias)
    }


    override fun setString(
        key: String,
        value: String
    ){
        val encryptedString = KeyStoreHelper.encryptString(keyStore, keyStoreAlias, value)
        super.setString(key, encryptedString)
    }


    override fun getString(
        key: String,
        fallbackVal: String
    ): String {
        val encryptedString= super.getString(key, fallbackVal)
        return KeyStoreHelper.decryptString(keyStore, keyStoreAlias, encryptedString)
    }
}