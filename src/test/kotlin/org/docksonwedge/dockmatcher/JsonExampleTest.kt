package org.docksonwedge.dockmatcher


import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.docksonwedge.dockmatcher.constants.TestConstants
import org.docksonwedge.dockmatcher.model.Pet
import org.docksonwedge.dockmatcher.model.Status
import org.docksonwedge.kotmatcher.DockMatcher
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class JsonExampleTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            RestAssured.baseURI = TestConstants.petStoreUrl
        }
    }

    @Test
    fun `Test getting a Pet and evaluating the JSON string`() {
        // We can share validation between tests by parameterizing our extension function
        // or we can inline define our checks which will be type-safe at compile time!
        DockMatcher(Pet::class)
            .check(TestConstants.corePetValidation(12001L, Status.AVAILABLE))
            .check {
                assertThat(tags[0].name).isNotBlank
                //if reliant on assertions, just default to `true`, although you lose some compile time type-checking
                true
            }.onBody(
                RestAssured.given()
                    .contentType(ContentType.JSON)
                    .get("/pet/{petId}", 12001L)
                    .body.asString()
            ) { "Tags was empty or id was not correct." } // optional message to return if a check passes assertions, but returns false
    }
}