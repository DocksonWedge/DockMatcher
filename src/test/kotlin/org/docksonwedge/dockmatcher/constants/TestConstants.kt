package org.docksonwedge.dockmatcher.constants

import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.docksonwedge.dockmatcher.model.Pet
import org.docksonwedge.dockmatcher.model.Status
import org.junit.jupiter.api.BeforeAll

object TestConstants {

    val defaultErrorMessage = "Failed to evaluate one of the boolean properties."
    val petStoreUrl = "https://petstore.swagger.io/v2"

    fun corePetValidation(testId: Long, testStatus: Status): Pet.() -> Boolean = {
        Assertions.assertThat(status).isEqualTo(testStatus)
        Assertions.assertThat(category.id).isGreaterThan(0)
        tags.isNotEmpty() && id == testId
    }
}