package io.patamon.eventbus

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/10/27
 */
fun main(args: Array<String>) {
    val bus = EventBus()
    val s = MySubscriber()
    bus.regist(s)


    bus.post(Event())
}