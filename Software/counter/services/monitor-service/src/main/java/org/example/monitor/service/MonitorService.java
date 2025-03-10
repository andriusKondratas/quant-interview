package org.example.monitor.service;

import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.component.Executable;
import org.example.common.service.ExecutorService;
import org.example.monitor.component.SourceDirectoryResolver;
import org.example.monitor.component.UnprocessedFilesCaptor;
import org.example.monitor.component.executor.CsvFileParseExecutor;
import org.example.monitor.component.executor.FileRenameExecutor;
import org.example.monitor.configuration.MonitorProperties;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MonitorService implements ExecutorService {

  private final MonitorProperties monitorProperties;
  private final SourceDirectoryResolver sourceDirectoryResolver;
  private final UnprocessedFilesCaptor unprocessedFilesCaptor;

  private final CsvFileParseExecutor csvFileParseExecutor;
  private final FileRenameExecutor fileRenameExecutor;

  private Path sourcePath;

  @Override
  public void execute() {
    var cycleStartDateTime = ZonedDateTime.now(ZoneOffset.UTC);
    log.info("Scan cycle STARTED");

    log.info("Resolving source and target directories");
    sourcePath = sourceDirectoryResolver.resolveSourceDirectory();

    log.info("Capturing unprocessed files");
    var unprocessedFiles = unprocessedFilesCaptor.captureUnprocessedFiles(sourcePath, monitorProperties.getFileBatchLimit());
    log.debug("Unprocessed file count: {}", unprocessedFiles.size());

    try {
      unprocessedFiles
          .forEach(file -> processFile(sourcePath, file));
    } catch (Exception e) {
      log.error("Scan cycle FAILED", e);
    }

    log.info("Scan cycle COMPLETE. Time taken: {} ms",
        ChronoUnit.MILLIS.between(cycleStartDateTime, ZonedDateTime.now(ZoneOffset.UTC)));
  }

  private void processFile(Path sourceDirectory, Path file) {
    var success = new AtomicBoolean(true);

    getExecutors().entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(exe -> {
          if (success.get()) {
            var stepStartDateTime = ZonedDateTime.now(ZoneOffset.UTC);
            success.set(exe.getValue().execute(sourceDirectory, file));
            exe.getValue().log(log, stepStartDateTime);
          }
        });
  }

  @Override
  public Map<Integer, Executable> getExecutors() {
    var executors = new HashMap<Integer, Executable>();

    executors.put(0, csvFileParseExecutor);
    executors.put(1, fileRenameExecutor);

    return executors;
  }
}