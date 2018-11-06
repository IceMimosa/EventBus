package io.patamon.eventbus.demo;

import com.google.common.eventbus.EventBus;

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/10/25
 */
public class TestEventsBus {
    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        eventBus.register(new MySubscriber());

        eventBus.post(new Event());
    }
}

