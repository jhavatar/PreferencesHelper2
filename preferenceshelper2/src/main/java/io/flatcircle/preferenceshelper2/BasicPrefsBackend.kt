package io.flatcircle.preferenceshelper2

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

open class BasicPrefsBackend(context: Context) : PrefsBackend {

    protected val sharedPrefs : SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun contains(key: String): Boolean {
        return sharedPrefs.contains(key)
    }

    override fun remove(key: String) {
        sharedPrefs.edit().remove(key).apply()
    }

    override fun setLong(key: String, value: Long) {
        sharedPrefs.edit().putLong(key, value).apply()
    }

    override fun getLong(key: String, fallbackVal: Long): Long {
        return sharedPrefs.getLong(key, fallbackVal)
    }

    override fun setInt(
        key: String,
        value: Int
    ) {
        sharedPrefs.edit().putInt(key, value).apply()
    }

    override fun getInt(key: String, fallbackVal: Int): Int {
        return sharedPrefs.getInt(key, fallbackVal)
    }

    override fun setFloat(key: String, value: Float) {
        sharedPrefs.edit().putFloat(key, value).apply()
    }

    override fun getFloat(key: String, fallbackVal: Float): Float {
        return sharedPrefs.getFloat(key, fallbackVal)
    }

    override fun setString(
        key: String,
        value: String
    ){
        sharedPrefs.edit().putString(key, value).apply()
    }

    override fun getString(
        key: String,
        fallbackVal: String
    ): String {
        return sharedPrefs.getString(key, fallbackVal)
    }

    override fun setBoolean(
        key: String,
        value: Boolean
    ) {
        sharedPrefs.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(
        key: String,
        fallbackVal: Boolean
    ): Boolean {
        return sharedPrefs.getBoolean(key, fallbackVal)
    }

    override fun clear() {
        sharedPrefs.edit().clear().apply()
    }
}