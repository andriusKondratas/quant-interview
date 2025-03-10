package org.example.discovery.component.executor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.example.common.component.Executable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileCreateExecutor implements Executable {

  @Override
  public boolean execute(Path directory, Path file) {
    if (!Files.exists(directory)) {
      try {
        Files.createDirectories(directory);
      } catch (IOException e) {
        log.error("Unable to create target directory {} ", directory, e);
      }
    }

    if (!Files.exists(file)) {
      try {
        Files.createFile(file);
      } catch (IOException e) {
        log.error("Unable to create report file {} ", file, e);
      }
    }

    return true;
  }
}
