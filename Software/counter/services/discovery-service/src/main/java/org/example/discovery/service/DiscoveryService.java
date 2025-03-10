package org.example.discovery.service;

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
import org.example.discovery.component.TargetDirectoryResolver;
import org.example.discovery.component.executor.FileCreateExecutor;
import org.example.discovery.component.executor.FileHeaderReportExecutor;
import org.example.discovery.component.executor.FileTopConnectionsReportExecutor;
import org.example.discovery.component.executor.HousekeeperExecutor;
import org.example.discovery.configuration.DiscoveryProperties;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscoveryService implements ExecutorService {

  private final DiscoveryProperties discoveryProperties;
  private final TargetDirectoryResolver targetDirectoryResolver;

  private final FileCreateExecutor fileCreateExecutor;
  private final FileHeaderReportExecutor fileHeaderReportExecutor;
  private final FileTopConnectionsReportExecutor fileTopConnectionsReportExecutor;
  private final HousekeeperExecutor housekeeperExecutor;

  @Override
  public void execute() {
    var cycleStartDateTime = ZonedDateTime.now(ZoneOffset.UTC);
    log.info("Discovery cycle STARTED");
    log.debug("[Lag ms: {}]", discoveryProperties.getLagMs());

    log.info("Resolving report target directory and file to report");
    var targetPath = targetDirectoryResolver.resolveTargetDirectory();
    var targetFile = targetDirectoryResolver.resolveTargetFile();
    log.debug("[Target Directory: {}]", targetPath);
    log.debug("[Target File: {}]", targetFile);

    discover(targetPath, targetFile);

    log.info("Discovery cycle COMPLETE. Time taken: {} ms",
        ChronoUnit.MILLIS.between(cycleStartDateTime, ZonedDateTime.now(ZoneOffset.UTC)));
  }

  private void discover(Path targetDirectory, Path file) {
    var success = new AtomicBoolean(true);

    getExecutors().entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(exe -> {
          if (success.get()) {
            var stepStartDateTime = ZonedDateTime.now(ZoneOffset.UTC);
            success.set(exe.getValue().execute(targetDirectory, file));
            exe.getValue().log(log, stepStartDateTime);
          }
        });
  }

  @Override
  public Map<Integer, Executable> getExecutors() {
    var executors = new HashMap<Integer, Executable>();

    executors.put(0, fileCreateExecutor);
    executors.put(1, fileHeaderReportExecutor);
    executors.put(2, fileTopConnectionsReportExecutor);

    if (discoveryProperties.isHouseKeeperEnabled()) {
      executors.put(3, housekeeperExecutor);
    }

    return executors;
  }
}

