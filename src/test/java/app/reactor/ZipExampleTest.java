package app.reactor;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ZipExampleTest {

  @Test
  void resultsWithCompletableFut() {
    var zipExample = new ZipExample();

    var results = zipExample.resultsWithCompletableFut();
    assertTrue(results.contains("Name Darsan has stats 98"));
    assertTrue(results.contains("Name Bhav has stats 89"));
    assertTrue(results.contains("Name Pras has stats 95"));
    assertTrue(results.contains("Name Div has stats 99"));
  }

  @Test
  void resultsWithFlux() {
    var zipExample = new ZipExample();

    var results = zipExample.resultsWithFlux().block();
    assertTrue(results.contains("Name Darsan has stats 98"));
    assertTrue(results.contains("Name Bhav has stats 89"));
    assertTrue(results.contains("Name Pras has stats 95"));
    assertTrue(results.contains("Name Div has stats 99"));
  }

  @Test
  void resultsWithFluxStepverifier() {
    var zipExample = new ZipExample();

    StepVerifier.create(zipExample.resultsWithFlux())
        .expectNext(
            List.of(
                "Name Darsan has stats 98",
                "Name Pras has stats 95",
                "Name Bhav has stats 89",
                "Name Div has stats 99"))
        .expectComplete()
        .verify();
  }
}
