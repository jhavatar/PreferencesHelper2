# PreferencesHelper2
Functions to help with checking your app's online status

[![](https://jitpack.io/v/flatcircle/PreferencesHelper2.svg)](https://jitpack.io/#flatcircle/PreferencesHelper2)

This is a Kotlin-only library which relies heavily on generics and type inference to make writing to and reading from Android SharedPreferences as easy as possible. For example:

```kotlin
    val prefs = Prefs(BasicPrefsBackend(this))
    prefs.set("key_name", "Flat Circle")
    val ourName: String = prefs.get("key_name")
```

Or for encrypted strings:
```kotlin
    val prefs = Prefs(EncryptStringPrefsBackend("ExampleAlias", this))
    prefs.set("key_name", "Flat Circle")
    val ourName: String = prefs.get("key_name")
```

Installation
--------

```groovy
implementation 'io.flatcircle:preferenceshelper:{version}'
```

Usage
-----

You can set and get any primitive or basic custom class into your sharedPreferences via PreferencesHelper. The supported classes are the same as [Moshi's supported classes](https://github.com/square/moshi#built-in-type-adapters) since it uses moshi to serialized. The supported functions are:

| Function, PreferencesHelper.  | Description | Example |
| ------------- | ------------- | ------------- |
| set(key, value) | Saves a given value to sharedPreferences, where [value] is any primitive, or a custom class that can be serialized by Moshi into Json | [Example](https://github.com/flatcircle/PreferencesHelper/blob/master/app/src/main/java/io/flatcircle/preferencehelper2example/MainActivity.kt)  |
| get<Any?>(key, defaultValue?)  | Returns any value you've saved, with optional DefaultValue. | [Example](https://github.com/flatcircle/PreferencesHelper/blob/master/app/src/main/java/io/flatcircle/preferencehelper2example/MainActivity.kt)  |
| contains(key) | Determines whether a value has been saved to sharedPreferences | [Example](https://github.com/flatcircle/PreferencesHelper/blob/master/app/src/main/java/io/flatcircle/preferencehelper2example/MainActivity.kt)  |
| remove(key) | Removes value associated with key from sharedPreferences | [Example](https://github.com/flatcircle/PreferencesHelper/blob/master/app/src/main/java/io/flatcircle/preferencehelper2example/MainActivity.kt)  |
| clear() | Remove all values in sharedPreferences | [Example](https://github.com/flatcircle/PreferencesHelper/blob/master/app/src/main/java/io/flatcircle/preferencehelper2example/MainActivity.kt)  |
| addCustomAdapter<Class>(adapter) | Adds a [custom moshi adapter](https://github.com/square/moshi#custom-type-adapters) for a given class | [Example](https://github.com/flatcircle/PreferencesHelper/blob/master/app/src/main/java/io/flatcircle/preferencehelper2example/MainActivity.kt)  |

If you are using custom classes and adapters and there is nothing to get, you will get Null Pointer exceptions unless you pass a defaultValue or use .getSafely() when you get your custom class.

The PreferencesHelper library also comes with basic KeyStore management for encryption/decryption purposes. The actual encryption works, but uses a currently deprecated method of generating a keypair that needs to be double-checked. You can encrypt and decrypt strings as follows:

| Function, KeyStoreHelper.  | Description | Example |
| ------------- | ------------- | ------------- |
| setAlias("YourAppAlias") | Sets a KeyStore alias for your app, in order to use encryption. Do this in your Application{onCreate(){}} | [Example](https://github.com/flatcircle/PreferencesHelper/blob/master/app/src/main/java/io/flatcircle/preferencehelper2example/MainActivity.kt)  |
| encryptString(context, string) | Returns an encrypted version of a given string | [Example](https://github.com/flatcircle/PreferencesHelper/blob/master/app/src/main/java/io/flatcircle/preferencehelper2example/MainActivity.kt)  |
| decryptString(context, string) | Decrypts a given string that was encrypted with above method. | [Example](https://github.com/flatcircle/PreferencesHelper/blob/master/app/src/main/java/io/flatcircle/preferencehelper2example/MainActivity.kt)  |

You can also use the Prefs(context) class if you don't want to pass the context every time. It requires that you .clear() the Prefs() class at the end of your app/activity lifecycle to avoid memory leaks, so use with care.

You can see an example of the Prefs class being instantiated, used and cleared [Here](https://github.com/flatcircle/PreferencesHelper/blob/master/app/src/main/java/io/flatcircle/preferencehelper2example/MainActivity.kt)

I know it's tempting to think you can use this as a replacement for a database, but please don't. When you start to serialize lists of custom classes you should really just create a database module to handle that.


Delegates
-----

You can use delegates to set up a variable which automatically loads from sharedPreferences and automatically saves to SharedPreferences every time that it changes.

```kotlin
    val prefs = Prefs(this)
    
    var name = ObservablePreferences(prefs, "name_key", "Jack")
    
    println(name) // prints "Smith", unless your SharedPreferences is empty, then it prints "Jack"
    name = "Smith"
    println(name) //prints "Smith"
```
