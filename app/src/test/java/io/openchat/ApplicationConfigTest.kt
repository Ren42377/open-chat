package io.openchat

import org.junit.Assert.assertEquals
import org.junit.Test

class ApplicationConfigTest {
    @Test
    fun packageIdentifierMatchesExpectedNamespace() {
        assertEquals("io.openchat", MainActivity::class.java.packageName)
    }
}
