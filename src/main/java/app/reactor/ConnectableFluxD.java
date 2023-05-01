package app.reactor;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import static app.reactor.Utils.sleep;

public class ConnectableFluxD {

  public static void main(String[] args) {
    connectableFluxD();
  }

  static void connectableFluxD() {
    Flux<Integer> source =
        Flux.range(1, 3).doOnSubscribe(s -> System.out.println("subscribed to source:" + s));
    ConnectableFlux<Integer> connectableFlux = source.publish();

    connectableFlux.subscribe(System.out::println, e-> {}, ()->{});
    connectableFlux.subscribe(System.out::println, e-> {}, ()->{});

    System.out.println("done subscribing");
    sleep(500);
    System.out.println("Will now connect");
    connectableFlux.connect();
  }
}
