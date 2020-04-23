package io.flatcircle.preferencehelper2example

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import io.flatcircle.preferenceshelper2.*

class MainActivity : AppCompatActivity() {

    lateinit var prefs: Prefs

    val basicPrefs : Prefs by lazy {
        Prefs(BasicPrefsBackend(this))
    }

    val encryptPrefs : Prefs by lazy {
        Prefs(EncryptStringPrefsBackend("ExampleAlias", this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val stringKey = "key_string"
        val string = "hello"
        PreferencesHelper.set(basicPrefs.backend, stringKey, string)
        val stringAgain = PreferencesHelper.get<String>(basicPrefs.backend, stringKey)
        val matchString = string == stringAgain

        val encryptStringKey = "key_encrypt_string"
        val encryptString = "allo allo this is nighthawk"
        PreferencesHelper.set(encryptPrefs.backend, encryptStringKey, encryptString)
        val encryptAgain = PreferencesHelper.get<String>(encryptPrefs.backend, encryptStringKey)
        val matchEncryptedStorage = encryptString == encryptAgain
        Log.d(MainActivity::class.java.simpleName, "encryptString = $encryptString, decryptedString = $encryptAgain")

//        /**
//         * You can also use the Prefs(context) class if you don't want to pass the context every time,
//         * but requires clearing. Use with dependency injection and/or coupled with lifecycle of activity/app
//         */
//        PreferenceManager.getDefaultSharedPreferences(this).getBoolean("hi", true)
//
//        val prefffs = PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("hi", true).apply()

        val integerKey = "key_integer"
        val integer = 451
        basicPrefs.set(integerKey, integer)
        val integerAgain = basicPrefs.get<Int>(integerKey)
        val matchInt = integer == integerAgain

        val longKey = "key_long"
        val long = 6942069L
        basicPrefs.set(longKey, long)
        val longAgain = basicPrefs.get<Long>(longKey)
        val matchLong = long == longAgain

        val booleanKey = "key_boolean"
        val boolean = true
        basicPrefs.set(booleanKey, boolean)
        val booleanAgain = basicPrefs.get<Boolean>(booleanKey)
        val matchBoolean = boolean == booleanAgain

        val customKey = "key_custom_class"
        val customClass = CustomClass(string, integer)
        basicPrefs.set(customKey, customClass)
        val customAgain = basicPrefs.get(customKey, CustomClass("default", -1))
        val matchCustom = customClass == customAgain

        val customierKey = "key_customier_class"
        val customierClass = CustomierCustomClass("AA", 2)
        basicPrefs.addCustomAdapter<CustomierCustomClass>(CustomierAdapter())
        basicPrefs.set(customierKey, customierClass)
        val customierAgain = basicPrefs.get(customierKey, CustomierCustomClass("BB", -1))
        val matchCustomier = customierClass == customierAgain

//        val stringUnencrypted = "U357 is on the way"
//        val stringEncrypted = KeyStoreHelper.encryptString(this, stringUnencrypted)
//        val stringDecrypted = KeyStoreHelper.decryptString(this, stringEncrypted)
//        val matchEncryption = stringUnencrypted == stringDecrypted

        val matchAll = matchString && matchEncryptedStorage && matchInt && matchLong &&
            matchBoolean && matchCustom && matchCustomier// && matchEncryption

        textView.text = "All classes have been stored and obtained successfully? \n " +
                "$matchAll ($matchString, $matchEncryptedStorage, $matchInt, $matchLong, $matchBoolean, $matchCustom, $matchCustomier)"
    }

    data class CustomClass(val string: String, val integer: Int)
    data class CustomierCustomClass(val string: String, val integer: Int)

    class CustomierAdapter {
        @ToJson
        fun toJson(customierClass: CustomierCustomClass): String {
            return customierClass.string + customierClass.integer
        }

        @FromJson
        fun fromJson(jsonSource: String): CustomierCustomClass {
            if (jsonSource.length != 3) throw JsonDataException("Invalid input")

            val string = jsonSource.substring(0, 2)
            val integer = jsonSource.substring(2, 3)
            return CustomierCustomClass(string, integer.toInt())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        prefs.clear() // Prefs must be cleared to avoid leaks
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
