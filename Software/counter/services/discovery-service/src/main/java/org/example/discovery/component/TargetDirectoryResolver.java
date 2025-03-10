package org.example.discovery.component;

import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.discovery.configuration.DiscoveryProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TargetDirectoryResolver {

  private final DiscoveryProperties discoveryProperties;

  public Path resolveTargetDirectory() {
    return Path.of(discoveryProperties.getPath());
  }

  public Path resolveTargetFile() {
    return resolveTargetDirectory().resolve(discoveryProperties.getFileName());
  }
}
