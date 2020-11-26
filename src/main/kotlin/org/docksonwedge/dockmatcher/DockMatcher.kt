package org.docksonwedge.kotmatcher

import org.assertj.core.api.Assertions.assertThat
import java.util.function.Supplier
import kotlin.reflect.KClass

class DockMatcher<T : Any>(private val clazz: KClass<T>) {

    private val checks = mutableListOf<T.() -> Boolean>()
    fun check(predicate: T.() -> Boolean): DockMatcher<T> {
        checks.add(predicate)
        return this
    }

    fun <Y> check(value: Y, field: T.() -> Y) {

        //checks.add { field.invoke(it)  == value }
    }

    fun assert(
        resultObj: T,
        message: Supplier<String> = Supplier { "Failed to evaluate one of the boolean properties." }
    ) {
        assertThat(checks.all { it.invoke(resultObj) })
            .withFailMessage(message)
            .isTrue
    }
}