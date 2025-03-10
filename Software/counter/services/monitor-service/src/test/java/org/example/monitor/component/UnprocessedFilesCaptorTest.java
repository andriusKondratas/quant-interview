package org.example.monitor.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

import org.example.monitor.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

public class UnprocessedFilesCaptorTest extends BaseIntegrationTest {

  @InjectMocks
  UnprocessedFilesCaptor fileCaptureExecutor;

  @Test
  void should_capture_unprocessed_files() {
    var sourcePath = initializeTestData(baseDirectory, "files-captor");

    doReturn(false).when(monitorProperties).isScanEnabled();

    var unProcessedFiles = fileCaptureExecutor.captureUnprocessedFiles(sourcePath,5);
    assertEquals(2, unProcessedFiles.size());
    assertTrue(unProcessedFiles.stream().allMatch(f -> f.getFileName().toString().endsWith(".csv")));
  }

}
