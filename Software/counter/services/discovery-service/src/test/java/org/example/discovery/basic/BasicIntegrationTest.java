package org.example.discovery.basic;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.awaitility.Durations;
import org.example.discovery.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

public class BasicIntegrationTest extends BaseIntegrationTest {

  @Test
  void should_expose_actuator_endpoint() {
    webClient.get()
        .uri("/actuator")
        .exchange()
        .expectStatus().isOk()
        .expectBody();
  }

  @Test
  public void should_run_scheduled_discovery_service() {
    doReturn(baseDirectory.toAbsolutePath() + "/source").when(discoveryProperties).getPath();
    await().ignoreExceptions().atMost(Durations.FIVE_SECONDS).untilAsserted(() -> verify(tasks, atLeast(4)).scan());
  }

}
