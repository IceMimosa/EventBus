package io.patamon.eventbus;

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

    @Subscribe
    public void a(String s) {

    }

    public static void main(String[] args) throws Exception {
        com.sun.tools.javac.Main.main(new String[] {"-proc:only",
                "-processor", "io.patamon.eventbus.processor.EventBusProcessor",
                "/Users/icemimosa/Documents/github/AskMisa/EventBus/src/main/kotlin/io/patamon/eventbus/Tests.java"});
    }


    /**
     * 方法处理
     */
}
