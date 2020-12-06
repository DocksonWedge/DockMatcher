package org.docksonwedge.dockmatcher

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.GsonBuilder
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.assertj.core.api.Condition
import org.docksonwedge.dockmatcher.constants.TestConstants
import org.docksonwedge.dockmatcher.model.Order
import org.docksonwedge.dockmatcher.model.OrderNotAnnotated
import org.docksonwedge.kotmatcher.DockMatcher
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class GsonSerializerTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            RestAssured.baseURI = TestConstants.petStoreUrl
            TestConstants.orderSetup()
        }
    }

    @Test
    fun `Test Gson deserialization`() {
        // we have a class with no annotation, so we try to use Gson deserializer
        val matcher = DockMatcher(OrderNotAnnotated::class)
        // you can configure the GsonBuilder by accessing the underlying serializer
        matcher.getGsonBuilder().setLenient()
        matcher
            .check {
                // if shipDate changes, say to a string, we we ill now at compile-time,
                // we don't need to run the test to see the failure.
                Assertions.assertThat(shipDate).`as`("The ship date year").hasYear(2020)
                Assertions.assertThat(petId).`is`(
                    Assertions.anyOf(
                        Condition({ it > 1200L }, "PetId is in range"), //      these conditions are made up for
                        Condition({ it == 0L }, "PetId default is allowed"), // demonstration purposes
                    )
                )
                Assertions.assertThat(quantity).isGreaterThan(0L)
            }.onBody(
                RestAssured.given()
                    .contentType(ContentType.JSON)
                    .get("/store/order/{orderId}", 12001L)
            )
    }
}