package org.docksonwedge.dockmatcher.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Status() {
    @SerialName("available") AVAILABLE,
    @SerialName("pending") PENDING,
    @SerialName("sold ") SOLD
}