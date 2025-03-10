package org.example.monitor.component.executor;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.example.common.component.Executable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileRenameExecutor implements Executable {

  @Override
  public boolean execute(Path directory, Path file) {
    try {
      addSuffix(file, "_processed");
      return true;
    } catch (IOException e) {
      log.error("Unable to rename file {} ", file, e);
    }
    return true;
  }

  public void addSuffix(Path file, String suffix) throws IOException {
    if (!file.getFileName().toString().contains(suffix)) {
      if (Files.exists(file)) {
        Files
            .move(file, file.resolveSibling(file + suffix), REPLACE_EXISTING);
      }
    }
  }
}
