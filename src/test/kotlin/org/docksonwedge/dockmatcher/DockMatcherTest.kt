package org.docksonwedge.kotmatcher

import org.assertj.core.api.Assertions.assertThat
import org.docksonwedge.dockmatcher.constants.TestConstants
import org.docksonwedge.kotmatcher.model.TestClass
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import java.lang.AssertionError

class DockMatcherTest {

    @TestFactory
    fun `ClassMatcher evaluates objects PASS checks correctly`() = listOf(
        TEST_CASE {
            assertThat(string).isNotBlank
            int == 1
        } to TestClass(1, "STRING"),
        TEST_CASE {
            assertThat(string).isBlank
            int >= -1
        } to TestClass(),
        TEST_CASE {
            assertThat(list).allMatch { it as Int > 0 }
            true
        } to TestClass(list = listOf(100, 3, 1))
    ).map { (matcher, testClass) ->
        DynamicTest.dynamicTest(
            "when I evaluate '$matcher' against $testClass I get no error"
        ) {
            matcher.onBody(testClass)
        }
    }

    @TestFactory
    fun `ClassMatcher evaluates objects FAIL checks correctly`() = listOf(
        Triple(TEST_CASE { int == 1 }, TestClass(-1, "STRING"), ""),
        Triple(TEST_CASE {
            assertThat(string).isBlank
            true
        }, TestClass(1, "STRING"), "Expecting blank but was:<\"STRING\">"),
        Triple(TEST_CASE {
            assertThat(string).isNotBlank
            int == 1
        }, TestClass(0, "STRING"), "Test message checking bool return!"),
        Triple(TEST_CASE {
            assertThat(string).isLessThan("b")
            int == 1
        }, TestClass(1, "c"), "Expecting: <\"c\">to be less than: <\"b\">")
    ).map { (matcher, testClass, message) ->
        DynamicTest.dynamicTest(
            "when I evaluate '$matcher' against $testClass I get an error with message '$message'"
        ) {
            var expectedMessage = ""
            val error = assertThrows<AssertionError> {
                if (message.isNotBlank()) {
                    expectedMessage = message
                    matcher.onBody(testClass) { message }
                } else {
                    expectedMessage = TestConstants.defaultErrorMessage
                    matcher.onBody(testClass)
                }
            }
            assertThat(expectedMessage).isEqualTo(error.message?.replace("\r\n", "")?.trim())
        }
    }

    private fun TEST_CASE(check: TestClass.() -> Boolean): DockMatcher<TestClass> {
        return DockMatcher(TestClass::class).check(check)
    }
}