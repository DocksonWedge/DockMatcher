package org.docksonwedge.dockmatcher.model

import kotlinx.serialization.Serializable

@Serializable
enum class Status() {
    AVAILABLE, PENDING, SOLD
}