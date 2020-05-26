package io.flatcircle.preferenceshelper2

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by jacquessmuts on 2019-05-24
 */
class PreferencesHelperTest {

    companion object {
        const val bilboString1 = """{"age":111,"name":"Bilbo"}"""
        const val bilboString2 = """{"name":"Bilbo","age":111}"""
    }

    private val rightClass = TestyClass("Bilbo", 111)
    private val wrongClass = TestyClass("Frodo", 35)

    @Test
    fun testGetSetBoolean() {
        val prefs = mockk<PrefsBackend>(relaxed = true)
        every {prefs.getBoolean("doesNotExist", true) } returns true
        every {prefs.getBoolean("doesNotExist", false) } returns false
        every {prefs.getBoolean("exists", false) } returns true

        assertFalse(PreferencesHelper.get(prefs, "doesNotExist", false))
        verify { prefs.getBoolean("doesNotExist", false) }

        assertTrue(PreferencesHelper.get(prefs, "doesNotExist", true))
        verify { prefs.getBoolean("doesNotExist", true) }

        assertFalse(PreferencesHelper.get<Boolean>(prefs, "doesNotExist", null))
        verify { prefs.getBoolean("doesNotExist", false) }

        assertTrue(PreferencesHelper.get<Boolean>(prefs, "exists", null))
        verify { prefs.getBoolean("exists", false) }

        confirmVerified(prefs)
    }


    @Test
    fun testGetSetInt() {
        val prefs = mockk<PrefsBackend>(relaxed = true)
        every {prefs.getInt("doesNotExist", 5) } returns 5
        every {prefs.getInt("doesNotExist", 0) } returns 0
        every {prefs.getInt("exists", 6) } returns 6
        every {prefs.getInt("exists", 0) } returns 6

        assertEquals(PreferencesHelper.get(prefs, "doesNotExist", 5), 5)
        verify { prefs.getInt("doesNotExist", 5) }

        assertEquals(PreferencesHelper.get(prefs, "doesNotExist", 0), 0)
        verify { prefs.getInt("doesNotExist", 0) }

        assertEquals(PreferencesHelper.get<Int>(prefs, "doesNotExist", null), 0)
        verify { prefs.getInt("doesNotExist", 0) }

        assertEquals(PreferencesHelper.get(prefs, "exists", 6), 6)
        verify { prefs.getInt("exists", 6) }

        assertEquals(PreferencesHelper.get<Int>(prefs, "exists", null), 6)
        verify { prefs.getInt("exists", 0) }

        confirmVerified(prefs)
    }


    @Test
    fun testGetSetLong() {
        val prefs = mockk<PrefsBackend>(relaxed = true)
        every {prefs.getLong("doesNotExist", 5L) } returns 5L
        every {prefs.getLong("doesNotExist", 0L) } returns 0L
        every {prefs.getLong("exists", 6L) } returns 6L
        every {prefs.getLong("exists", 0L) } returns 6L

        assertEquals(PreferencesHelper.get(prefs, "doesNotExist", 5L), 5L)
        verify { prefs.getLong("doesNotExist", 5L) }

        assertEquals(PreferencesHelper.get(prefs, "doesNotExist", 0L), 0L)
        verify { prefs.getLong("doesNotExist", 0L) }

        assertEquals(PreferencesHelper.get(prefs, "doesNotExist", null as Long?), 0L)
        verify { prefs.getLong("doesNotExist", 0L) }

        assertEquals(PreferencesHelper.get(prefs, "exists", 6L), 6L)
        verify { prefs.getLong("exists", 6L) }

        assertEquals(PreferencesHelper.get(prefs, "exists", null as Long?), 6L)
        verify { prefs.getLong("exists", 0L) }

        confirmVerified(prefs)
    }



    @Test
    fun testGetSetFloat() {
        val prefs = mockk<PrefsBackend>(relaxed = true)
        every {prefs.getFloat("doesNotExist", 5f) } returns 5f
        every {prefs.getFloat("doesNotExist", 0f) } returns 0f
        every {prefs.getFloat("exists", 6f) } returns 6f
        every {prefs.getFloat("exists", 0f) } returns 6f

        assertEquals(PreferencesHelper.get(prefs, "doesNotExist", 5f), 5f)
        verify { prefs.getFloat("doesNotExist", 5f) }

        assertEquals(PreferencesHelper.get(prefs, "doesNotExist", 0f), 0f)
        verify { prefs.getFloat("doesNotExist", 0f) }

        assertEquals(PreferencesHelper.get(prefs, "doesNotExist", null as Float?), 0f)
        verify { prefs.getFloat("doesNotExist", 0f) }

        assertEquals(PreferencesHelper.get(prefs, "exists", 6f), 6f)
        verify { prefs.getFloat("exists", 6f) }

        assertEquals(PreferencesHelper.get(prefs, "exists", null as Float?), 6f)
        verify { prefs.getFloat("exists", 0f) }

        confirmVerified(prefs)
    }


    @Test
    fun testGetSetString() {
        val prefs = mockk<PrefsBackend>(relaxed = true)
        every {prefs.getString("doesNotExist", "foo") } returns "foo"
        every {prefs.getString("doesNotExist", "") } returns ""
        every {prefs.getString("exists", "bar") } returns "bar"
        every {prefs.getString("exists", "") } returns "bar"

        assertEquals(PreferencesHelper.get(prefs, "doesNotExist", "foo"), "foo")
        verify { prefs.getString("doesNotExist", "foo") }

        assertEquals(PreferencesHelper.get(prefs, "doesNotExist", ""), "")
        verify { prefs.getString("doesNotExist", "") }

        assertEquals(PreferencesHelper.get(prefs, "doesNotExist", null as String?), "")
        verify { prefs.getString("doesNotExist", "") }

        assertEquals(PreferencesHelper.get(prefs, "exists", "bar"), "bar")
        verify { prefs.getString("exists", "bar") }

        assertEquals(PreferencesHelper.get(prefs, "exists", null as String?), "bar")
        verify { prefs.getString("exists", "") }

        confirmVerified(prefs)
    }


    @Test
    fun serializeIntoString() {
        val rightString = PreferencesHelper.serializeIntoString(rightClass)
        System.out.println("serializeIntoString: rightString = $rightString")
        assertTrue((rightString == bilboString1) || (rightString == bilboString2))

        val wrongString = PreferencesHelper.serializeIntoString(wrongClass)
        assertFalse(wrongString == bilboString1)
        assertFalse(wrongString == bilboString2)

//        val nullString = PreferencesHelper.serializeIntoString(null as TestyClass?)
//        System.out.println("serializeIntoString: nullString = $nullString")
    }


    @Test
    fun serializeFromString() {
        val deserializedClass = PreferencesHelper.serializeFromString(bilboString1, wrongClass)
        assertEquals(rightClass, deserializedClass)

        val defaultClass = PreferencesHelper.serializeFromString("", rightClass)
        assertEquals(defaultClass, rightClass)
    }

    data class TestyClass(val name: String, val age: Int)
}