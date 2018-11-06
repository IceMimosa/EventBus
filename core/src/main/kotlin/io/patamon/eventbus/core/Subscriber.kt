package io.patamon.eventbus.core

import java.lang.reflect.Method

/**
 * Desc: A @[Subscribe] method class
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/10/27
 */
data class Subscriber(
        // 调用的目标对象
        private val target: Any,
        // 目标对象的方法
        private val method: Method
) {

    /**
     * 直接通过反射的方式调用
     */
    fun invoke(vararg args: Any) {
        this.method.invoke(this.target, *args)
    }

    /**
     * 通过原生的方式调用
     */
    fun invokeNative(vararg args: Any) {
        val type = "${method.name}@${args.first().javaClass.typeName}"
        (this.target as EventBusHandler).`$$__invoke__$$`(type, args.first())
    }
}