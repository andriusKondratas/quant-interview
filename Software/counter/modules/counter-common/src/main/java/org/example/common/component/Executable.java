package org.example.common.component;

import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;

public interface Executable {

  boolean execute(Path directory, Path file);

  default void log(Logger log, ZonedDateTime startDateTime) {
    log.info("  Cycle step [{}] COMPLETE. Time taken: {} ms", this.getClass().getSimpleName(),
        ChronoUnit.MILLIS.between(startDateTime, ZonedDateTime.now(ZoneOffset.UTC)));
  }
}
