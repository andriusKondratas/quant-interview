package org.example.discovery.component.executor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.component.Executable;
import org.example.common.component.TimeDrifter;
import org.example.common.service.CounterStore;
import org.example.discovery.configuration.DiscoveryProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileTopConnectionsReportExecutor implements Executable {

  final TimeDrifter timeDrifter;
  final DiscoveryProperties discoveryProperties;
  final CounterStore counterStore;

  @Override
  public boolean execute(Path directory, Path file) {
    var currentTime = timeDrifter.getCurrentTime(discoveryProperties.getLagMs());
    var topDomains = counterStore.findTopDomains(currentTime.minusMinutes(1), discoveryProperties.getTopCount());

    for (int i = 0; i < topDomains.size(); i++) {
      var row = String.format("%s. ", i+1).concat(topDomains.get(i).toString())
          .concat("\r\n");
      try {
        Files.write(
            file,
            row.getBytes(),
            StandardOpenOption.APPEND);
      } catch (IOException e) {
        log.error("Unable to append row to file {} ", file, e);
      }
    }
    return true;
  }
}
