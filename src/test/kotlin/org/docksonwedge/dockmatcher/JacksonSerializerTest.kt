package org.docksonwedge.dockmatcher

import com.fasterxml.jackson.databind.DeserializationFeature
import io.restassured.RestAssured
import io.restassured.http.ContentType
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
        }
    }

    @Test
    fun `Test getting a Pet and evaluating the JSON string`() {
        // we have a class not annotated with @Serializable from kotlinx, so we try to use Jackson ObjectMapper
        val matcher = DockMatcher(Order::class)
        // you can configure the Jackson Object mapper by accessing the underlying serializer
        matcher.objectMapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true)
        matcher
            .check {
                assertThat(shipDate).`as`("The ship date year").hasYear(2020)
                assertThat(petId).`is`(
                    anyOf(
                        Condition({ it > 1200L }, "PetId is in range"), //      these conditions are made up for
                        Condition({ it == 0L }, "PetId default is allowed"), // demonstration purposes
                    )
                )
                status.isNotBlank()
            }.onBody(
                RestAssured.given()
                    .contentType(ContentType.JSON)
                    .get("/store/order/{orderId}", 12001L)
            )
    }
}