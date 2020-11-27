package org.docksonwedge.dockmatcher.constants

import org.assertj.core.api.Assertions.assertThat
import org.docksonwedge.dockmatcher.model.pet.Pet
import org.docksonwedge.dockmatcher.model.Status

object TestConstants {

    val defaultErrorMessage = "Failed to evaluate one of the boolean properties."
    val petStoreUrl = "https://petstore.swagger.io/v2"

    fun corePetValidation(testId: Long, testStatus: Status): Pet.() -> Boolean = {
        assertThat(status).isEqualTo(testStatus)
        assertThat(category.id).isGreaterThan(0)
        tags.isNotEmpty() && id == testId
    }
}