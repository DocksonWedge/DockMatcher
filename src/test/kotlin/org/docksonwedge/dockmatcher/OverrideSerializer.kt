package org.docksonwedge.dockmatcher

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.docksonwedge.dockmatcher.constants.TestConstants
import org.docksonwedge.dockmatcher.model.Status
import org.docksonwedge.dockmatcher.model.pet.Category
import org.docksonwedge.dockmatcher.model.pet.Pet
import org.docksonwedge.kotmatcher.DockMatcher
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class OverrideSerializer {
    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            RestAssured.baseURI = TestConstants.petStoreUrl
        }
    }

    @Test
    fun `Deserialized by kotlinx due to @Serializable annotation on Pet class`() {
        val response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .get("/pet/{petId}", 12001L)


        DockMatcher(Pet::class) {
            // We can override the deserializer with our own if we'd like
            Pet(
                -1,
                Category(1, "Example"),
                "Example pet",
                listOf("photo"),
                listOf(),
                Status.SOLD
            )
        }
            .check {
                Assertions.assertThat(tags).isEmpty()
                id == -1L
            }.onBody(response)
    }
}