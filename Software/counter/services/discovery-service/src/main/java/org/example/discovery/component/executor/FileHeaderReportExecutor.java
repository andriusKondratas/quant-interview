package org.example.discovery.component.executor;

import static org.example.common.component.TimeDrifter.DATE_TIME_FORMAT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.component.Executable;
import org.example.common.component.TimeDrifter;
import org.example.discovery.configuration.DiscoveryProperties;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class FileHeaderReportExecutor implements Executable {

  final TimeDrifter timeDrifter;
  final DiscoveryProperties discoveryProperties;

  @Override
  public boolean execute(Path directory, Path file) {
    var currentTime = timeDrifter.getCurrentTime(discoveryProperties.getLagMs());
    var header = String.format("# Top %s Domains - ", discoveryProperties.getTopCount())
        .concat(DATE_TIME_FORMAT.format(currentTime))
        .concat("\r\n");

    try {
      Files.write(
          file,
          header.getBytes(),
          StandardOpenOption.APPEND);
    } catch (IOException e) {
      log.error("Unable to append header to file {} ", file, e);
    }

    return true;
  }
}
