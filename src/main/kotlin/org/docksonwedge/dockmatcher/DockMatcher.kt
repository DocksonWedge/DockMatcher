package org.docksonwedge.kotmatcher

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.response.Response
import org.assertj.core.api.Assertions.assertThat
import java.util.function.Supplier
import kotlin.reflect.KClass

/**
 * Use to evaluate a Json body, RestAssured response, or POKO on tests
 * with a compile-time validated DSL. See [org.docksonwedge.dockmatcher.JsonExampleTest],
 * [org.docksonwedge.dockmatcher.ObjectExampleTest], or [org.docksonwedge.dockmatcher.RestAssuredExampleTest]
 * for example usage.
 *
 * @author  DocksonWedge
 * @param clazz the Kotlin class that you are validating
 * @param objectMapper The default jackson object mapper to use. You can override the default with your own
 * ObjectMapper, or call .configure on the objectMapper property to change the deserialization properties.
 * By default FAIL_ON_UNKNOWN_PROPERTIES is set to true.
 *
 * Only used when using `assert` with a Json String.
 * @param deserializer Override for the desrializer if you don't want to use Jackson ObjectMapper.
 * Overriding this obviates the objectMapper parameter and configuration.
 *
 * Only used when using `assert` with a Json String.
 */
class DockMatcher<T : Any>(
    private val clazz: KClass<T>,
    val objectMapper: ObjectMapper = ObjectMapper(),
    private val deserializer: (String) -> T = { objectMapper.readValue(it, clazz.java) }
) {
    // Default deserialization config
    init {
        objectMapper
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(ACCEPT_CASE_INSENSITIVE_ENUMS, true)
    }

    private val checks = mutableListOf<T.() -> Boolean>()
    private val defaultFailureMessage = "Failed to evaluate one of the boolean properties."

    /**
     * Add a check to the list of assertions to evaluate. Returning false will throw an assertion error, but you are
     * also encouraged to use your own assertions, for example with assertj.
     *
     * This version validates a plain old kotlin object against the stored checks.
     *
     * @return the current DockMatcher object so you can chain multiple `check` calls together and call `assert`
     * as part of the same call chain.
     */
    fun check(predicate: T.() -> Boolean): DockMatcher<T> {
        //TODO allow per-check error message
        checks.add(predicate)
        return this
    }

    /**
     * Add a check to the list of assertions to evaluate. Returning false will throw an assertion error, but you are
     * also encouraged to use your own assertions, for example with assertj.
     *
     * This version validates a plain old Kotlin object against the stored checks.
     */
    fun onBody(
        resultObj: T,
        message: Supplier<String> = Supplier { defaultFailureMessage }
    ) {
        assertThat(checks.all { it.invoke(resultObj) })
            .withFailMessage(message)
            .isTrue
    }

    /**
     * Add a check to the list of assertions to evaluate. Returning false will throw an assertion error, but you are
     * also encouraged to use your own assertions, for example with assertj.
     *
     * This version validates a Json string against the stored checks. The string must be deserializable by the
     * `deserializer` function in the class constructor.
     */
    fun onBody(
        resultJson: String,
        message: Supplier<String> = Supplier { defaultFailureMessage }
    ) {
        //TODO - test
        onBody(
            deserializer.invoke(resultJson),
            message
        )
    }

    /**
     * Add a check to the list of assertions to evaluate. Returning false will throw an assertion error, but you are
     * also encouraged to use your own assertions, for example with assertj.
     *
     * This version validates the body of a RestAssured `Response` object against the stored checks. The string must
     * be deserializable by RestAssured.
     */
    fun onBody(
        resultRestAssuredResponse: Response,
        message: Supplier<String> = Supplier { defaultFailureMessage }
    ) {
        //TODO - test
        onBody(
            resultRestAssuredResponse.body.`as`<T>(clazz.java) as T,
            message
        )
    }
}