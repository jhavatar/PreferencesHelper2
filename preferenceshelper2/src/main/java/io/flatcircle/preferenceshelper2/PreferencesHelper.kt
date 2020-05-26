@file:Suppress("unused")

package io.flatcircle.preferenceshelper2

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.reflect.KClass

object PreferencesHelper {

    private val customAdapters: MutableList<Pair<KClass<out Any>, Any>> = mutableListOf()

    /**
     * This function adds a custom adapter for a given class. Adapter is applied to the Moshi serializer.
     */
    inline fun <reified T : Any> addCustomAdapter(adapter: Any) {
        addCustomAdapter(adapter, T::class)
    }
    @PublishedApi
    internal fun <T : Any> addCustomAdapter(adapter: Any, clazz: KClass<T>) {
        val nuAdapterPair = Pair(clazz, adapter)
        val indexOfExistingAdapter = customAdapters.indexOfFirst { it.first == clazz }
        if (indexOfExistingAdapter < 0) {
            customAdapters.add(nuAdapterPair)
        } else {
            customAdapters[indexOfExistingAdapter] = nuAdapterPair
        }
    }

    /**
     * Get a given object from sharedpreferences with the given key.
     *
     * @param prefsBackend any PrefsBackend
     * @param key PrefsBackend String Key
     * @param default is optional, unless you are getting a custom object
     */
    @Throws(IllegalArgumentException::class)
    inline fun <reified T : Any> get(prefsBackend: PrefsBackend, key: String, default: T? = null): T {
        return get(prefsBackend, key, default, T::class)
    }

    /**
     * Same as get, but enforces null safety on custom objects via the default parameter
     */
    inline fun <reified T : Any> getSafely(prefsBackend: PrefsBackend, key: String, default: T?): T {
        return get(prefsBackend, key, default, T::class)
    }

    @Suppress("UNCHECKED_CAST")
    @PublishedApi
    internal fun <T : Any> get(prefsBackend: PrefsBackend, key: String, default: T?, clazz: KClass<T>): T {
        return when (clazz) {
            Long::class -> getLong(prefsBackend, key, default as Long? ?: 0L) as T
            Int::class -> getInt(prefsBackend, key, default as Int? ?: 0) as T
            Float::class -> getFloat(prefsBackend, key, default as Float? ?: 0.0f) as T
            String::class -> getString(prefsBackend, key, default as String? ?: "") as T
            Boolean::class -> getBoolean(prefsBackend, key, default as? Boolean ?: false) as T
            else -> {
                if (default == null) throw IllegalArgumentException("Custom objects require a default parameter to be passed")
                serializeFromString(getString(prefsBackend, key, ""), clazz, default)
            }
        }
    }

    /**
     * Sets the given key value pair in Preferences. Uses apply, so returns before finishing.
     */
    inline fun <reified T : Any> set(prefsBackend: PrefsBackend, key: String, value: T) {
        set(prefsBackend, key, value, T::class)
    }


    @PublishedApi
    internal fun <T : Any> set(prefsBackend: PrefsBackend, key: String, value: T, clazz: KClass<T>) {
        return when (clazz) {
            Long::class -> setLong(prefsBackend, key, value as Long)
            Int::class -> setInt(prefsBackend, key, value as Int)
            Float::class -> setFloat(prefsBackend, key, value as Float)
            String::class -> setString(prefsBackend, key, value as String)
            Boolean::class -> setBoolean(prefsBackend, key, value as Boolean)
            else -> setString(prefsBackend, key, serializeIntoString(value, clazz))
        }
    }


    /**
     * Takes a given class and serializes it into a JSON String using Moshi. May require setting a
     * custom adapter via addCustomAdapter
     */
    inline fun <reified T : Any> serializeIntoString(input: T): String {
        return serializeIntoString(input, T::class)
    }
    @PublishedApi
    internal fun <T : Any> serializeIntoString(input: T, clazz: KClass<T>): String {
        val moshiBuilder = Moshi.Builder()
        val indexOfAdapter = customAdapters.indexOfFirst { it.first == clazz }
        if (indexOfAdapter >= 0) {
            moshiBuilder.add(customAdapters[indexOfAdapter].second)
        }
        val moshi = moshiBuilder.add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<T> = moshi.adapter(clazz.java)
        return jsonAdapter.toJson(input)
    }

    /**
     * Takes a given Json String and serializes it into the required object.
     *
     * @param default is required in case of invalid string or serialization errors
     */
    inline fun <reified T : Any> serializeFromString(input: String, default: T): T {
        return serializeFromString(input, T::class, default)
    }

    @PublishedApi
    @Throws(IllegalArgumentException::class)
    internal fun <T : Any> serializeFromString(input: String, clazz: KClass<T>, default: T): T {
        if (input.isEmpty())
            return default

        val moshiBuilder = Moshi.Builder()
        val indexOfAdapter = customAdapters.indexOfFirst { it.first == clazz }
        if (indexOfAdapter >= 0) {
            moshiBuilder.add(customAdapters[indexOfAdapter].second)
        }
        val moshi = moshiBuilder.add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<T> = moshi.adapter(clazz.java)
        try {
            return jsonAdapter.fromJson(input) ?: default
        } catch (e: JsonEncodingException) {
            throw java.lang.IllegalArgumentException("Unable to serialize $input into $clazz due to... ${e.localizedMessage}")
        }
    }

    /**
     * Determines if the preferences contains a value for the given key
     */
    fun contains(prefsBackend: PrefsBackend, key: String): Boolean {
        return prefsBackend.contains(key)
    }

    internal fun setLong(prefsBackend: PrefsBackend, key: String, value: Long) {
        return prefsBackend.setLong(key, value)
    }

    internal fun getLong(prefsBackend: PrefsBackend, key: String, fallbackVal: Long): Long {
        return prefsBackend.getLong(key, fallbackVal)
    }

    internal fun setInt(prefsBackend: PrefsBackend, key: String, value: Int) {
        return prefsBackend.setInt(key, value)
    }

    internal fun getInt(prefsBackend: PrefsBackend, key: String, fallbackVal: Int): Int {
        return prefsBackend.getInt(key, fallbackVal)
    }

    internal fun setFloat(prefsBackend: PrefsBackend, key: String, value: Float) {
        return prefsBackend.setFloat(key, value)
    }

    internal fun getFloat(prefsBackend: PrefsBackend, key: String, fallbackVal: Float): Float {
        return prefsBackend.getFloat(key, fallbackVal)
    }

    internal fun setString(prefsBackend: PrefsBackend, key: String, value: String) {
        return prefsBackend.setString(key, value)
    }

    internal fun getString(prefsBackend: PrefsBackend, key: String, fallbackVal: String): String {
        return prefsBackend.getString(key, fallbackVal)
    }

    internal fun setBoolean(prefsBackend: PrefsBackend, key: String, value: Boolean) {
        return prefsBackend.setBoolean(key, value)
    }

    internal fun getBoolean(prefsBackend: PrefsBackend, key: String, fallbackVal: Boolean): Boolean {
        return prefsBackend.getBoolean(key, fallbackVal)
    }

    fun remove(prefsBackend: PrefsBackend, key: String){
        return prefsBackend.remove(key)
    }

    fun clear(prefsBackend: PrefsBackend) {
        prefsBackend.clear()
    }
}