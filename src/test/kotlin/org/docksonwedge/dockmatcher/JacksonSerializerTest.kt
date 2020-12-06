package org.docksonwedge.dockmatcher

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import io.restassured.http.ContentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.Condition
import org.docksonwedge.dockmatcher.constants.TestConstants
import org.docksonwedge.dockmatcher.model.Order
import org.docksonwedge.kotmatcher.DockMatcher
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class JacksonSerializerTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            RestAssured.baseURI = TestConstants.petStoreUrl
            TestConstants.orderSetup()
        }
    }

    @Test
    fun `Test jackson object mapper config`() {
        // we have a class not annotated with @Serializable from kotlinx, so we try to use Jackson ObjectMapper
        val matcher = DockMatcher(Order::class)
        // you can configure the Jackson Object mapper by accessing the underlying serializer
        matcher.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        matcher
            .checkBool {
                // if shipDate changes, say to a string, we we ill now at compile-time,
                // we don't need to run the test to see the failure.
                assertThat(shipDate).`as`("The ship date year").hasYear(2020)
                assertThat(petId).`is`(
                    anyOf(
                        Condition({ it > 1200L }, "PetId is in range"), //      these conditions are made up for
                        Condition({ it == 0L }, "PetId default is allowed"), // demonstration purposes
                    )
                )
                quantity >= 0
            }.onBody(
                RestAssured.given()
                    .contentType(ContentType.JSON)
                    .get("/store/order/{orderId}", 12001L)
            )
    }
}