package io.patamon.eventbus

import kotlin.reflect.KClass


fun KClass<*>?.flattenHierarchy(): Set<KClass<*>> {
    this ?: emptySet<KClass<*>>()
    val types = mutableSetOf<KClass<*>>()
    var t = this
    while (t != Any::class) {
        types.add(t!!)
        t = (t.java.superclass as Class<*>).kotlin
    }
    return types
}
