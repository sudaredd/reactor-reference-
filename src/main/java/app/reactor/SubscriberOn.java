package app.reactor;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Random;

import static reactor.core.scheduler.Schedulers.DEFAULT_BOUNDED_ELASTIC_QUEUESIZE;
import static reactor.core.scheduler.Schedulers.DEFAULT_BOUNDED_ELASTIC_SIZE;


public class SubscriberOn {
    public static void main(String[] args) {
        Scheduler s = Schedulers.newBoundedElastic(10, 3, "bounded-scheduler");
//        s = Schedulers.newBoundedElastic(DEFAULT_BOUNDED_ELASTIC_SIZE, DEFAULT_BOUNDED_ELASTIC_QUEUESIZE, "bounded-scheduler");

        final Flux<String> flux = Flux.interval(Duration.ofMillis(100))
            .take(1000)
            .map(i -> getVal(i))
            .subscribeOn(s)
            .map(i -> "value " + i + " by " + Thread.currentThread().getName());
        for (int i = 0; i < 1000; i++) {
            final int x = i;
            flux.subscribe(s1 -> System.out.println(x+"=>number of active threads :" + Thread.activeCount() + "=>" + s1));
        }
        //System.exit(0);
    }

    private static String getVal(long i) {
        return 10 + i + Thread.currentThread().getName();
    }
}
