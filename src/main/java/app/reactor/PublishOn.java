package app.reactor;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;


public class PublishOn {
    public static void main(String[] args) {
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);

        final Flux<String> flux = Flux
            .range(1, 2)
            .map(i -> 10 + i + Thread.currentThread().getName())
            .publishOn(s)
            .map(i -> "value " + i + " "+ Thread.currentThread().getName());
        flux.subscribe(System.out::println);
//        new Thread(() -> flux.subscribe(System.out::println)).start();
    }
}
