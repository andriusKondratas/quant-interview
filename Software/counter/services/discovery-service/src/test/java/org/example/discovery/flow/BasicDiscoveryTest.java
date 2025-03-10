package org.example.discovery.flow;

import static org.example.common.component.TimeDrifter.DATE_TIME_FORMAT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import org.example.common.model.NetworkConnectionDto;
import org.example.discovery.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

public class BasicDiscoveryTest extends BaseIntegrationTest {


  @Test
  void should_create_report_file() {

    //Switch off scheduler
    doReturn(false).when(discoveryProperties).isReportEnabled();
    doReturn(baseDirectory.toAbsolutePath() + "/source").when(discoveryProperties).getPath();

    discoveryService.execute();

    var processedFiles = listProcessedFiles(baseDirectory.toAbsolutePath().resolve("source"));
    assertEquals(1, processedFiles.size());
    assertTrue(processedFiles.stream().allMatch(f->f.getFileName().toString().endsWith(discoveryProperties.getFileName())));
  }

  @Test
  void should_contain_correct_row_count() throws IOException {

    //Switch off scheduler
    doReturn(false).when(discoveryProperties).isReportEnabled();
    doReturn(baseDirectory.toAbsolutePath() + "/source").when(discoveryProperties).getPath();

    discoveryService.execute();

    var processedFiles = listProcessedFiles(baseDirectory.toAbsolutePath().resolve("source"));
    assertEquals(1, processedFiles.size());
    assertTrue(processedFiles.stream().allMatch(f->f.getFileName().toString().endsWith(discoveryProperties.getFileName())));

    assertTrue(processedFiles.stream().findFirst().isPresent());
    var processedFile = processedFiles.stream().findFirst().get();
    var lines = FileUtils.readLines(processedFile.toFile(), StandardCharsets.UTF_8);
    assertEquals(1, lines.size());
  }

  @Test
  void should_contain_correct_header_row() throws IOException {

    //Switch off scheduler
    doReturn(false).when(discoveryProperties).isReportEnabled();
    doReturn(baseDirectory.toAbsolutePath() + "/source").when(discoveryProperties).getPath();
    doReturn(8).when(discoveryProperties).getTopCount();

    LocalDateTime currentTime = LocalDateTime.of(2024, 2, 21, 21, 15, 0, 0);
    when(timeDrifter.getCurrentTime(anyLong())).then(invocation -> currentTime);


    discoveryService.execute();

    var processedFiles = listProcessedFiles(baseDirectory.toAbsolutePath().resolve("source"));
    assertEquals(1, processedFiles.size());
    assertTrue(processedFiles.stream().allMatch(f->f.getFileName().toString().endsWith(discoveryProperties.getFileName())));

    assertTrue(processedFiles.stream().findFirst().isPresent());
    var processedFile = processedFiles.stream().findFirst().get();
    var lines = FileUtils.readLines(processedFile.toFile(), StandardCharsets.UTF_8);
    assertEquals(1, lines.size());

    var reportLine = lines.get(0);
    assertEquals(String.format("# Top %s Domains - %s", 8, DATE_TIME_FORMAT.format(currentTime)), reportLine);
  }

  @Test
  void should_contain_correct_top_row() throws IOException {

    //Switch off scheduler
    doReturn(false).when(discoveryProperties).isReportEnabled();
    doReturn(baseDirectory.toAbsolutePath() + "/source").when(discoveryProperties).getPath();
    doReturn(8).when(discoveryProperties).getTopCount();

    LocalDateTime currentTime = LocalDateTime.of(2024, 2, 10, 21, 15, 0, 0);
    when(timeDrifter.getCurrentTime(anyLong())).then(invocation -> currentTime);

    addTestNetworkConnection("1708867200000","192.168.1.10","5000","10.0.0.5","443","example.com");
    addTestNetworkConnection("1708867205000","192.168.1.11","5001","10.0.0.6","80","test.com");
    addTestNetworkConnection("1708867206000","192.168.1.11","5001","10.0.0.6","80","test.com");
    addTestNetworkConnection("1708867207000","192.168.1.11","5001","10.0.0.6","80","test.com");
    addTestNetworkConnection("1708867210000","192.168.1.12","5002","10.0.0.7","443","example.com");

    discoveryService.execute();

    var processedFiles = listProcessedFiles(baseDirectory.toAbsolutePath().resolve("source"));
    assertEquals(1, processedFiles.size());
    assertTrue(processedFiles.stream().allMatch(f->f.getFileName().toString().endsWith(discoveryProperties.getFileName())));

    assertTrue(processedFiles.stream().findFirst().isPresent());
    var processedFile = processedFiles.stream().findFirst().get();
    var lines = FileUtils.readLines(processedFile.toFile(), StandardCharsets.UTF_8);
    assertEquals(3, lines.size());

    var reportHeaderLine = lines.get(0);
    assertEquals(String.format("# Top %s Domains - %s", 8, DATE_TIME_FORMAT.format(currentTime)), reportHeaderLine);

    var reportTopLine1 = lines.get(1);
    assertEquals("1. test.com - 3 connections", reportTopLine1);

    var reportTopLine2 = lines.get(2);
    assertEquals("2. example.com - 2 connections", reportTopLine2);
  }

  private void addTestNetworkConnection(String... arguments){
    var networkConnectionDto = new NetworkConnectionDto();
    var networkConnectionDtoList = new ArrayList<NetworkConnectionDto>();
    networkConnectionDto.setTimestamp(Long.parseLong(arguments[0]));
    networkConnectionDto.setSrc_ip(arguments[1]);
    networkConnectionDto.setSrc_port(arguments[2]);
    networkConnectionDto.setDst_ip(arguments[3]);
    networkConnectionDto.setDst_port(arguments[4]);
    networkConnectionDto.setDomain(arguments[5]);

    networkConnectionDtoList.add(networkConnectionDto);
    counterStore.sinkConnections(networkConnectionDtoList);
  }
}