package org.docksonwedge.kotmatcher

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.internal.mapping.GsonMapper
import io.restassured.internal.mapping.Jackson2Mapper
import io.restassured.mapper.ObjectMapperDeserializationContext
import io.restassured.path.json.mapper.factory.GsonObjectMapperFactory
import io.restassured.response.Response
import org.assertj.core.api.Assertions.assertThat
import java.io.InputStream
import java.util.function.Supplier
import kotlin.reflect.KClass

class DockMatcher<T : Any>(
    private val clazz: KClass<T>,
    private val deserializer: (String) -> T = { ObjectMapper().readValue(it, clazz.java) }
)
{

    private val checks = mutableListOf<T.() -> Boolean>()
    fun check(predicate: T.() -> Boolean): DockMatcher<T> {
        checks.add(predicate)
        return this
    }

    fun assert(
        resultObj: T,
        message: Supplier<String> = Supplier { "Failed to evaluate one of the boolean properties." }
    ) {
        assertThat(checks.all { it.invoke(resultObj) })
            .withFailMessage(message)
            .isTrue
    }

    fun assert(
        resultJson: String,
        message: Supplier<String> = Supplier { "Failed to evaluate one of the boolean properties." }
    ) {
        //TODO - test
        assert(
            deserializer.invoke(resultJson),
            message
        )
    }

    fun assert(
        resultRestAssuredResponse: Response,
        message: Supplier<String> = Supplier { "Failed to evaluate one of the boolean properties." }
    ) {
        //TODO - test
        assert(
            resultRestAssuredResponse.body.`as`<T>(clazz.java) as T,
            message
        )
    }
}