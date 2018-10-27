package io.patamon.eventbus

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/10/27
 */
class EventBus(
        private val executor: Executor = Executors.newWorkStealingPool()
) {

    private val subscribers = ConcurrentHashMap<Class<Any>, Set<Subscriber>>()

    fun regist(obj: Any) {
        val subs = this.subscribers.getOrDefault(obj.javaClass, mutableSetOf())
        val types = obj::class.flattenHierarchy()
        println(types)
    }



}