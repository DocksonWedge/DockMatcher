package org.docksonwedge.dockmatcher.constants

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import io.restassured.http.ContentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.docksonwedge.dockmatcher.model.Order
import org.docksonwedge.dockmatcher.model.OrderNotAnnotated
import org.docksonwedge.dockmatcher.model.pet.Pet
import org.docksonwedge.dockmatcher.model.Status
import org.docksonwedge.dockmatcher.model.pet.Category
import org.docksonwedge.dockmatcher.model.pet.Tag
import java.util.*

object TestConstants {

    val defaultErrorMessage = "Failed to evaluate one of the boolean properties."
    val petStoreUrl = "https://petstore.swagger.io/v2"

    val testPet = Pet(
        2147483648L,
        Category(1, "Fun"),
        "Fido",
        listOf(),
        listOf(Tag(1, "default")),
        Status.AVAILABLE
    )
    val testOrder = Order(
        12001L,
        3000L,
        1,
        Calendar.Builder().setDate(2020,5,1).build().time,
        false
        )

    fun commonPetValidation(testId: Long, testStatus: Status): Pet.() -> Boolean = {
        assertThat(status).isEqualTo(testStatus)
        assertThat(category.id).isGreaterThan(0)
        tags.isNotEmpty() && id == testId
    }

    fun petSetup() {
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(Json.encodeToString(testPet))
            .post("/pet")
    }

    fun orderSetup(){
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(ObjectMapper().writeValueAsString(testOrder))
            .post("/store/order")
    }
}