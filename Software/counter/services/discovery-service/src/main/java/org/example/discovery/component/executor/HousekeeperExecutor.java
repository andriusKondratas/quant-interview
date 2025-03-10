package org.example.discovery.component.executor;

import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.component.Executable;
import org.example.common.service.CounterStore;
import org.example.discovery.configuration.DiscoveryProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HousekeeperExecutor implements Executable {

  final DiscoveryProperties discoveryProperties;
  final CounterStore counterStore;

  @Override
  public boolean execute(Path directory, Path file) {
    var dataRetentionInMinutes = discoveryProperties.getRetentionMinutes();
    log.info("Data retention in minutes: {}", dataRetentionInMinutes);

    var deletedConnectionCount = counterStore.cleanNetworkConnections(dataRetentionInMinutes);

    log.info("{} Network connection deleted", deletedConnectionCount);
    return true;
  }
}
