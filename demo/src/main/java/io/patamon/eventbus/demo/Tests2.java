package io.patamon.eventbus.demo;

import io.patamon.eventbus.EventBus;
import io.patamon.eventbus.core.Subscribe;

/**
 * Desc:
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/11/6
 */
public class Tests2 {

    @Subscribe
    public void handle(Event e) {
        System.out.println("执行了 handle e");
    }


    public static void main(String[] args) throws InterruptedException {
        EventBus bus = new EventBus();
        bus.regist(new Tests2());

        bus.post(new Event());

        Thread.sleep(2000L);
    }
}
