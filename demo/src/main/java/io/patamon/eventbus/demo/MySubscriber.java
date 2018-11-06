package io.patamon.eventbus.demo;

// import com.google.common.eventbus.Subscribe;

/**
 * Desc:
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/10/27
 */
public class MySubscriber extends MySubscriberS {

    // @Subscribe
    // @Subscribea
    @Override
    public void event01(Event event) {
        System.out.println("执行了 event01");
    }

    // @Subscribe
    // @Subscribe
    public void event02(Event event) {
        System.out.println("执行了 event02");
    }

}

class MySubscriberS {

    // @Subscribe
    // @Subscribe
    public void event01(Event event) {
        System.out.println("执行了 MySubscriberS event01");
    }

}