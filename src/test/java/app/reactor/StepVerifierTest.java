package app.reactor;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class StepVerifierTest {

  @Test
  public void testAppendBoomError() {

    Flux<String> source = Flux.just("thing1", "thing2");

    StepVerifier.create(appendBoomError(source))
        .expectNext("thing1")
        .expectNext("thing2")
        .expectErrorMessage("Boom")
        .verify();

    StepVerifier.create(appendBoom(source, "thing3"))
        .expectNext("thing1")
        .expectNext("thing2")
        .expectNext("thing3")
        .expectComplete()
        .verify();

    StepVerifier.create(appendBoom(source, "thing3"))
        .expectNext("thing1")
        .expectNextCount(2)
        .expectComplete()
        .verify();
  }

  @Test
  public void testSplitPathIsUsed() {
    StepVerifier.create(
            processOrFallback(Mono.just("just a phrase with tabs!"), Mono.just("EMPTY PHRASE")))
        .expectNext("just", "a", "phrase", "with", "tabs!")
        .expectComplete()
        .verify();
  }

  @Test
  public void testEmptyPathIsUsed() {
    StepVerifier.create(
            processOrFallback(Mono.empty(), Mono.just("EMPTY PHRASE")))
        .expectNext("EMPTY PHRASE")
        .expectComplete()
        .verify();
  }

  public <T> Flux<T> appendBoomError(Flux<T> source) {

    return source.concatWith(Mono.error(new IllegalArgumentException("Boom")));
  }

  public <T> Flux<T> appendBoom(Flux<T> source, T t) {

    return source.concatWith(Mono.just(t));
  }

  public Flux<String> processOrFallback(Mono<String> source, Publisher<String> fallback) {

    return source.flatMapMany(s -> Flux.fromArray(s.split("\\s"))).switchIfEmpty(fallback);
  }
}
