package org.docksonwedge.dockmatcher


import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.docksonwedge.dockmatcher.constants.TestConstants
import org.docksonwedge.dockmatcher.model.pet.Pet
import org.docksonwedge.dockmatcher.model.Status
import org.docksonwedge.kotmatcher.DockMatcher
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class KotlinxSerializerTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            RestAssured.baseURI = TestConstants.petStoreUrl
        }
        // todo - run setup queries
    }

    @Test
    fun `Deserialized by kotlinx due to @Serializable annotation on Pet class`() {
        val response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .get("/pet/{petId}", 2147483648L)

        // We can share validation between tests by parameterizing our extension function
        // or we can inline define our checks which will be type-safe at compile time!
        DockMatcher(Pet::class)
            .checkBool(TestConstants.commonPetValidation(2147483648L, Status.AVAILABLE))
            .check {
                assertThat(tags[0].name).isNotBlank
            }.onBody(response.body.asString()) { // You can pass a raw JSON string if not using REST-assured
                // optional message to return if a check passes assertions, but returns false
                "Tags was empty or id was not correct."
            }
    }
}