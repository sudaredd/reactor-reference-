package app.reactor;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import static app.reactor.Utils.sleep;

public class ParallelFlux {
  public static void main(String[] args) {
    //

    Flux.range(1, 10)
        .parallel(2)
        .subscribe(i -> System.out.println(Thread.currentThread().getName() + " -> " + i));

    /**
     * In order to tell the resulting ParallelFlux where to run each rail (and, by extension, to run
     * rails in parallel) you have to use runOn(Scheduler). Note that there is a recommended
     * dedicated Scheduler for parallel work: Schedulers.parallel().
     */

    Flux.range(1, 10)
        .parallel(2)
        .runOn(Schedulers.parallel())
        .subscribe(i -> System.out.println(Thread.currentThread().getName() + " -> " + i));

    sleep(100);
  }
}
