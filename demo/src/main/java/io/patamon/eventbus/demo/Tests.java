package io.patamon.eventbus.demo;

import java.io.Serializable;

/**
 * Desc:
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/11/5
 */
public class Tests implements Serializable {

    public static void main(String[] args) throws Exception {
        com.sun.tools.javac.Main.main(new String[] {"-proc:only",
                "-processor", "io.patamon.eventbus.processor.EventBusProcessor",
                "/Users/icemimosa/Documents/github/AskMisa/EventBus/demo/src/main/java/io/patamon/eventbus/demo/Tests.java"});
    }

}
