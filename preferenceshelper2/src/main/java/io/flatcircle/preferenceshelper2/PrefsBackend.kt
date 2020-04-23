package io.flatcircle.preferenceshelper2

interface PrefsBackend {
    fun contains(key: String): Boolean

    fun remove(key: String)

    fun setLong(key: String, value: Long)

    fun getLong(key: String, fallbackVal: Long): Long

    fun setInt(key: String, value: Int)

    fun getInt(key: String, fallbackVal: Int): Int

    fun setFloat(key: String, value: Float)

    fun getFloat(key: String, fallbackVal: Float): Float

    fun setString(key: String, value: String)

    fun getString(key: String, fallbackVal: String): String

    fun setBoolean(key: String, value: Boolean)

    fun getBoolean(key: String, fallbackVal: Boolean): Boolean

    fun clear()
}