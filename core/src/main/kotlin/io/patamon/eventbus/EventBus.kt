package io.patamon.eventbus

import io.patamon.eventbus.core.Subscribe
import io.patamon.eventbus.core.Subscriber
import io.patamon.eventbus.core.flattenHierarchy
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Desc: event bus core
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/10/27
 */
class EventBus(
        private val executor: Executor = Executors.newWorkStealingPool()
) {

    private val subscribers = ConcurrentHashMap<Class<*>, MutableSet<Subscriber>>()

    fun regist(obj: Any) {
        obj::class.flattenHierarchy().forEach { type ->
            type.java.declaredMethods
                    .filter { it.isAnnotationPresent(Subscribe::class.java) }
                    .forEach {
                        val pts = it.parameterTypes
                        if (pts.size != 1)
                            throw RuntimeException("Method $it has @Subscribe annotation but has ${pts.size} parameters. Subscriber methods must have exactly 1 parameter.")
                        val subs = this.subscribers.computeIfAbsent(pts[0]) { mutableSetOf() }
                        subs.add(Subscriber(obj, it))
                    }
        }
    }

    fun post(event: Any) {
        this.subscribers[event.javaClass]?.forEach {
            executor.execute {
                // it.invoke(event)
                it.invokeNative(event)
            }
        }
    }
}