package io.patamon.eventbus.demo;

import io.patamon.eventbus.core.EventBusHandler;
import io.patamon.eventbus.core.Subscribe;

import java.io.Serializable;

/**
 * Desc:
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/11/5
 */
public class Tests implements Serializable {

    public String ss;

    @Subscribe
    public void a(String s) {

    }

    public static void main(String[] args) throws Exception {
        com.sun.tools.javac.Main.main(new String[] {"-proc:only",
                "-processor", "io.patamon.eventbus.processor.EventBusProcessor",
                "/Users/icemimosa/Documents/github/AskMisa/EventBus/demo/src/main/kotlin/io/patamon/eventbus/demo/Tests.java"});
        System.out.println(EventBusHandler.class);
    }


}
