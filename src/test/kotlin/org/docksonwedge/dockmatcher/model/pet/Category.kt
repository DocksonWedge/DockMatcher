package org.docksonwedge.dockmatcher.model.pet

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int,
    val name: String
)
