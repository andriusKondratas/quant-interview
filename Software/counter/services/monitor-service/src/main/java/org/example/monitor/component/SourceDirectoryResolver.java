package org.example.monitor.component;

import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.monitor.configuration.MonitorProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SourceDirectoryResolver {

  private final MonitorProperties monitorProperties;

  public Path resolveSourceDirectory() {

    return Path.of(monitorProperties.getSourcePath());
  }
}
