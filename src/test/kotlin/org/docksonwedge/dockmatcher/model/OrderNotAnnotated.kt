package org.docksonwedge.dockmatcher.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class OrderNotAnnotated(
    val id: Long,
    val petId: Long,
    val quantity: Long,
    val shipDate: Date,
    val complete: Boolean
)