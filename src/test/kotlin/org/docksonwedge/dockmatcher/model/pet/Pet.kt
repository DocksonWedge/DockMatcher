package org.docksonwedge.dockmatcher.model.pet

import kotlinx.serialization.Serializable
import org.docksonwedge.dockmatcher.model.Status

/**
 * In reality, this should be the object used in the production code as the model object for deserializing
 * use the same serializer with DockMatcher
 * Doing things that way means when the real schema changes, the tests also update. If type is inferred in your
 * test's assertions, those tests will fail at compile time instead of runtime.
 */
@Serializable
data class Pet(
    val id: Long,
    val category: Category,
    val name: String,
    val photoUrls: List<String>,
    val tags: List<Tag>,
    val status: Status
)
