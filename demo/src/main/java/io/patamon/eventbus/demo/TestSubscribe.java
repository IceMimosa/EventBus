package io.patamon.eventbus.demo;

import io.patamon.eventbus.EventBus;
import io.patamon.eventbus.core.Subscribe;

/**
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/11/6
 */
public class TestSubscribe {

    @Subscribe
    public void handle(Event e) {
        System.out.println("执行了 handle");
    }

    @Subscribe
    public void handle2(Event e) {
        System.out.println("执行了 handle2");
    }


    public static void main(String[] args) throws InterruptedException {
        EventBus bus = new EventBus();
        bus.regist(new TestSubscribe());
        bus.post(new Event());

        Thread.sleep(1000L);
    }
}
