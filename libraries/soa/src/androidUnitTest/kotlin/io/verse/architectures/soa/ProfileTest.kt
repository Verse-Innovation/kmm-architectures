package io.verse.architectures.soa

import io.tagd.arch.datatype.Property
import org.junit.Test
import kotlin.test.assertEquals

class ProfileTest {

    private val profile = Profile()

    @Test
    fun `add value should store the value correctly`() {
        val key = "key"
        val value = "value"

        profile.set(name = key, value = value)

        assertEquals(value, profile.value(key))
    }

    @Test
    fun `add ProfileAttribute should store the ProfileAttribute correctly`() {
        val key = "key"
        val value = "value"

        profile.set(property = Property(name = key, value = value))

        assertEquals(value, profile.value(key))
    }

    @Test
    fun `value and attribute should return same value`() {
        val key = "key"
        val value = "value"

        profile.set(name = key, value = value)

        assertEquals(value, profile.value(key))
        assertEquals(value, profile.property<String>(key)?.value)
    }

}