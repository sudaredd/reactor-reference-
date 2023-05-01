package app.reactor;

import reactor.core.publisher.Flux;

import java.util.List;

public class FluxCreate {

    Flux<String> seq1 = Flux.just("foo", "bar", "foo-bar");
    Flux<String> seq2 = Flux.fromIterable(List.of("foo", "bar", "foo-bar"));
    Flux<Integer> numbersFrom5To7 = Flux.range(5, 8);
}
