package org.docksonwedge.dockmatcher.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable

@Serializable
data class Pet @JsonCreator constructor(
    @JsonProperty("id") val id: Long,
    @JsonProperty("category") val category: Category,
    @JsonProperty("name") val name: String,
    @JsonProperty("photoUrls") val photoUrls: List<String>,
    @JsonProperty("tags") val tags: List<Tag>,
    @JsonProperty("status") val status: Status
)
