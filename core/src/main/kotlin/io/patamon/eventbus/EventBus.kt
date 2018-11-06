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
        private val executor: Executor = Executors.newWorkStealingPool(),
        /**
         * 是否使用原生的方法调用, 非反射
         */
        private val nativeInvoke: Boolean = true
) {

    /**
     * 存放调用关系: `方法参数类型` -> 调用类集合
     */
    private val subscribers = ConcurrentHashMap<Class<*>, MutableSet<Subscriber>>()

    /**
     * 注册类中的所有方法
     */
    fun regist(obj: Any) {
        // 找出所有的父类, TODO: 处理子类和父类重复的方法
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

    /**
     * 发布事件
     */
    fun post(event: Any) {
        this.subscribers[event.javaClass]?.forEach {
            executor.execute {
                if (nativeInvoke) {
                    it.invokeNative(event)
                } else {
                    it.invoke(event)
                }
            }
        }
    }
}