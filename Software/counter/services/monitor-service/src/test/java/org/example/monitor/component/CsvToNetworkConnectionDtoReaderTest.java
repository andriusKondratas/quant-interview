package org.example.monitor.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

import org.example.monitor.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

public class CsvToNetworkConnectionDtoReaderTest extends BaseIntegrationTest {

  @InjectMocks
  CsvToNetworkConnectionDtoReader csvToNetworkConnectionDtoReader;

  @InjectMocks
  UnprocessedFilesCaptor fileCaptureExecutor;

  @Test
  void should_read_csv_file() {
    var sourcePath = initializeTestData(baseDirectory, "csv-reader");

    doReturn(false).when(monitorProperties).isScanEnabled();

    var unProcessedFiles = fileCaptureExecutor.captureUnprocessedFiles(sourcePath, 5);
    assertEquals(1, unProcessedFiles.size());
    assertTrue(unProcessedFiles.stream().allMatch(f -> f.getFileName().toString().endsWith(".csv")));

    unProcessedFiles.forEach(unProcessedFile -> {
      var connections = csvToNetworkConnectionDtoReader.read(unProcessedFile);
      assertEquals(3, connections.size());
    });
  }
}
