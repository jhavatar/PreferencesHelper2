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
implementation 'com.github.flatcircle:PreferencesHelper2:{version}'
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

You can see an example of the Prefs class being instantiated, used and cleared [Here](https://github.com/flatcircle/PreferencesHelper/blob/master/app/src/main/java/io/flatcircle/preferencehelper2example/MainActivity.kt)


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
