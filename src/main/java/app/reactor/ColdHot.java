package app.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;

import static app.reactor.Utils.sleep;
import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

public class ColdHot {

  public static void main(String[] args) {
//    new ColdHot().coldProcess();
    new ColdHot().hotProcess();
  }

  void coldProcess() {
    Flux<String> cold =
        Flux.fromIterable(List.of("blue", "green", "orange", "purple")).map(String::toUpperCase);
    cold.subscribe(
        f -> {
          System.out.println("subscribe1:"+f);
          sleep(150);
        });
    cold.subscribe(
        f -> {
          System.out.println("subscribe2:"+f);
          sleep(200);
        });
  }

  void hotProcess() {
    Sinks.Many<String> hotSource = Sinks.unsafe().many().multicast().directBestEffort();
    Flux<String> hotFlux = hotSource.asFlux().map(String::toUpperCase);

    hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: "+d));

    hotSource.emitNext("blue", FAIL_FAST);
    hotSource.tryEmitNext("green").orThrow();

    hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: "+d));

    hotSource.emitNext("yellow", FAIL_FAST);
    hotSource.emitNext("red", FAIL_FAST);
    hotSource.emitComplete(FAIL_FAST);
  }
}
