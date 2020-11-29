package org.docksonwedge.kotmatcher

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.restassured.response.Response
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.assertj.core.api.Assertions.assertThat
import java.util.function.Supplier
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType

/**
 * Use to evaluate a Json body, RestAssured response, or POKO on tests
 * with a compile-time validated DSL. See [org.docksonwedge.dockmatcher.JsonExampleTest],
 * [org.docksonwedge.dockmatcher.ObjectExampleTest], or [org.docksonwedge.dockmatcher.RestAssuredExampleTest]
 * for example usage.
 *
 * @author  DocksonWedge
 * @param clazz the Kotlin class that you are validating
 * @param overrideDeserializer Override for the desrializer if you don't want to use Jackson ObjectMapper.
 * Overriding this obviates the objectMapper parameter and configuration.
 *
 * Only used when using `assert` with a Json String.
 *
 * @property objectMapper The default Jackson object mapper to use. You can override the default with your own
 * function, or call .configure on the objectMapper property to change the deserialization properties.
 * By default FAIL_ON_UNKNOWN_PROPERTIES is set to true.
 *
 * Only used when using `assert` with a Json String with Jackson deserializer.
 *
 * @property gsonBuilder The default Gson builder to use. You can override the default with your own
 * function, or use the gsonBuilder property to change the deserialization properties.
 *
 * Only used when using `assert` with a Json String with Gson deserializer.
 */
class DockMatcher<T : Any>(
    private val clazz: KClass<T>,
    overrideDeserializer: ((String) -> T)? = null
) {
    private var objectMapper: ObjectMapper? = null
    private var gsonBuilder: GsonBuilder? = null

    val deserializer: (String) -> T = overrideDeserializer ?: getDefaultDeserializer()
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
        onBody(deserializer.invoke(resultJson), message)
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
        onBody(resultRestAssuredResponse.body.asString(), message)
    }

    /**
     * Use to configure deserialization settings when using Jackson ObjectMapper as the deserializer.
     * @throws RunTimeError if not using Jackson to deserialize
     */
    fun getObjectMapper(): ObjectMapper {
        return objectMapper
            ?: throw error("Cannot access objectMapper in DockMatcher. Deserializer is not using Jackson.")
    }

    /**
     * Use to configure deserialization settings when using Gson as the deserializer.
     * @throws RunTimeError if not using Gson to deserialize
     */
    fun getGsonBuilder(): GsonBuilder {
        return gsonBuilder
            ?: throw error("Cannot access gsonBuilder in DockMatcher. Deserializer is not using Gson.")
    }

    private fun getDefaultDeserializer(): (String) -> T {
        if (clazz.annotations.any { it is Serializable }) {
            return getDefaultKotlinxSerializer()
        } else if (clazz.constructors.flatMap { it.annotations }.any { it is JsonCreator }) {
            return getDefaultJacksonSerializer()
        } else {
            return getDefaultGsonSerializer()
        }
    }

    private inline fun getDefaultKotlinxSerializer(): (String) -> T {
        return { Json.decodeFromString(serializer(clazz.createType()), it) as T }
    }

    private inline fun getDefaultJacksonSerializer(): (String) -> T {
        objectMapper = ObjectMapper()
        getObjectMapper()
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(ACCEPT_CASE_INSENSITIVE_ENUMS, true)
        return { getObjectMapper().readValue(it, clazz.java) }
    }

    private inline fun getDefaultGsonSerializer(): (String) -> T {
        gsonBuilder = GsonBuilder()
        return { getGsonBuilder().create().fromJson(it, clazz.java) }
    }
}