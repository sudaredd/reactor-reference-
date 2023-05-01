package app.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ZipExample {

  public List<String> resultsWithCompletableFut() {
    CompletableFuture<List<String>> ids = ifhIds();

    CompletableFuture<List<String>> result =
        ids.thenComposeAsync(
            l -> {
              Stream<CompletableFuture<String>> zip =
                  l.stream()
                      .map(
                          i -> {
                            CompletableFuture<String> nameTask = ifhName(i);
                            CompletableFuture<Integer> statTask = ifhStat(i);

                            return nameTask.thenCombineAsync(
                                statTask, (name, stat) -> "Name " + name + " has stats " + stat);
                          });
              List<CompletableFuture<String>> combinationList = zip.collect(Collectors.toList());
              CompletableFuture<String>[] combinationArray =
                  combinationList.toArray(new CompletableFuture[combinationList.size()]);

              CompletableFuture<Void> allDone = CompletableFuture.allOf(combinationArray);
              return allDone.thenApply(
                  v ->
                      combinationList.stream()
                          .map(CompletableFuture::join)
                          .collect(Collectors.toList()));
            });
    return result.join();
  }

  public Mono<List<String>> resultsWithFlux() {

    var ids = Flux.fromIterable(ids());

    var combinations =
        ids.flatMap(
            id -> {
              Mono<String> name = ifhNameMono(id);
              Mono<Integer> stat = ifhStatMono(id);
              return name.zipWith(stat, (n, s) -> "Name " + n + " has stats " + s);
            });

    return combinations.collectList();
  }

  private Map<String, String> nameMap = Map.of("1", "Darsan", "2", "Pras", "3", "Bhav", "4", "Div");

  private Map<String, Integer> statMap = Map.of("1", 98, "2", 95, "3", 89, "4", 99);

  private CompletableFuture<Integer> ifhStat(String id) {
    return CompletableFuture.completedFuture(statMap.get(id));
  }

  private CompletableFuture<String> ifhName(String id) {
    return CompletableFuture.completedFuture(nameMap.get(id));
  }

  private Mono<Integer> ifhStatMono(String id) {
    return Mono.just(statMap.get(id));
  }

  private Mono<String> ifhNameMono(String id) {
    return Mono.just(nameMap.get(id));
  }

  private CompletableFuture<List<String>> ifhIds() {

    return CompletableFuture.supplyAsync(() -> ids());
  }

  private List<String> ids() {
    return List.of("1", "2", "3", "4");
  }
}
