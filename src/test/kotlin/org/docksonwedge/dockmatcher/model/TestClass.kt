package org.docksonwedge.kotmatcher.model

import kotlinx.serialization.Serializable

@Serializable
data class TestClass(val int: Int = 0, val string: String = "", val list: List<Int> = listOf())
