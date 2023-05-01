package app.reactor;

import reactor.core.publisher.Flux;

public class SubscriberEx {

  public static void main(String[] args) {
    fluxWithSubscribe(Flux.range(1, 3));

    Flux<Integer> numbersWithError =
        Flux.range(1, 4)
            .map(
                i -> {
                  if (i <= 3) return i;
                  else throw new RuntimeException("Got to 4");
                });
    fluxWithSubscribeAndError(numbersWithError);
    fluxWithSubscribeAndDone(Flux.range(1, 4));
  }

  private static void fluxWithSubscribe(Flux<Integer> integerFlux) {
    System.out.println("fluxWithSubscribe");
    integerFlux.subscribe(System.out::println);
  }

  private static void fluxWithSubscribeAndError(Flux<Integer> integerFlux) {
    System.out.println("fluxWithSubscribeAndError");
    integerFlux.subscribe(
        num -> System.out.println(num), error -> System.out.println("Error :" + error));
  }

  private static void fluxWithSubscribeAndDone(Flux<Integer> integerFlux) {
    System.out.println("fluxWithSubscribeAndErrorAndDone");
    integerFlux.subscribe(
        num -> System.out.println(num),
        error -> System.out.println("Error :" + error),
        () -> System.out.println("done"));
  }
}
