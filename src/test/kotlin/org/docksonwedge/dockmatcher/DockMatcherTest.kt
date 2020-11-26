package org.docksonwedge.kotmatcher

import org.assertj.core.api.Assertions.assertThat
import org.docksonwedge.kotmatcher.model.TestClass
import org.junit.jupiter.api.Test

class DockMatcherTest {

    @Test
    fun `ClassMatcher evaluates object check correctly`() {
        val matcher = DockMatcher(TestClass::class)
        matcher.check {
            assertThat(string).isNotBlank
            int == 1
        }
    }
}