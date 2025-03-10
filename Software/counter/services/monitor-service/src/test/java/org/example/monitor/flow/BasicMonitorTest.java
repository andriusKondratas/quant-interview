package org.example.monitor.flow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

import org.example.monitor.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

public class BasicMonitorTest extends BaseIntegrationTest {


  @Test
  void should_monitor_connections() {
    var sourcePath = initializeTestData(baseDirectory, "basic-flow");

    doReturn(false).when(monitorProperties).isScanEnabled();
    doReturn(baseDirectory.toAbsolutePath() + "/source").when(monitorProperties).getSourcePath();

    monitorService.execute();

    var processedFiles = listProcessedFiles(sourcePath);
    assertEquals(1, processedFiles.size());
    assertTrue(processedFiles.stream().allMatch(f->f.getFileName().toString().endsWith(".csv_processed")));

    var networkConnections = counterStore.findAllNetworkConnections();
    assertEquals(3, networkConnections.size());
  }
}
