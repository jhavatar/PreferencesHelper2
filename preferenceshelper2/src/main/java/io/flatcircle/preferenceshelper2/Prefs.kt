package io.flatcircle.preferenceshelper2

/**
 * Class for handling an instance with a reference to context. Only use this if you understand when
 * to instantiate and .clear() the Prefs class in the Android lifecycle.
 */
open class Prefs(val backend: PrefsBackend) {

    /**
     * Get a given object from sharedpreferences with the given key.
     *
     * @param key SharedPreferences String Key
     * @param default is optional, unless you are getting a custom object
     */
    @Throws(IllegalArgumentException::class)
    inline fun <reified T : Any> get(key: String, default: T? = null): T {
        return PreferencesHelper.get(backend, key, default, T::class)
    }

    /**
     * Sets the given key value pair in Preferences. Uses apply, so returns before finishing.
     */
    inline fun <reified T : Any> set(key: String, value: T) {
        PreferencesHelper.set(backend, key, value, T::class)
    }

    /**
     * Remove value for given key
     *
     * @param key SharedPreferences String Key
     */
    fun remove(key: String) {
        PreferencesHelper.remove(backend, key)
    }

    /**
     * Remove values
     */
    fun clear() {
        PreferencesHelper.clear(backend)
    }

    inline fun <reified T : Any> addCustomAdapter(adapter: Any) {
        PreferencesHelper.addCustomAdapter(adapter, T::class)
    }
}