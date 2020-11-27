package org.docksonwedge.dockmatcher.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Order @JsonCreator constructor(
    @JsonProperty("id") val id: Long,
    @JsonProperty("petId") val petId: Long,
    @JsonProperty("quantity") val quantity: Long,
    @JsonProperty("shipDate") val shipDate: Date,
    @JsonProperty("status") val status: String,
    @JsonProperty("complete") val complete: Boolean
)