package org.example.monitor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.example.common.service.CounterStore;
import org.example.monitor.configuration.ScheduledTasks;
import org.example.monitor.configuration.MonitorProperties;
import org.example.monitor.service.MonitorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {
    MonitorApplication.class})
@Slf4j
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class BaseIntegrationTest {

  @Autowired
  protected WebTestClient webClient;

  @SpyBean
  protected MonitorProperties monitorProperties;
  @SpyBean
  protected MonitorService monitorService;
  @SpyBean
  protected ScheduledTasks tasks;
  @SpyBean
  protected CounterStore counterStore;


  private static ClassLoader getClassLoader() {
    return BaseIntegrationTest.class.getClassLoader();
  }

  protected Path baseDirectory;

  @BeforeEach
  void setUp() throws IOException {
    baseDirectory = Files.createTempDirectory("base");
  }

  @AfterEach
  void cleanUp() throws IOException {
    FileUtils.deleteDirectory(baseDirectory.toFile());
    baseDirectory = null;
  }

  protected Path initializeTestData(Path rootTargetPath, String testCase) {
    Path sourcePath;
    try {
      var resource = getClassLoader().getResource(testCase + File.separator);
      assert resource != null;

      sourcePath = rootTargetPath
          .resolve("source");

      if (!Files.exists(sourcePath)) {
        Files.createDirectories(sourcePath);
      }

      FileUtils.copyDirectory(Paths.get(resource.toURI()).toFile(), sourcePath.toFile());
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException("Unable to prepare base directory for a test", e);
    }

    return sourcePath;
  }

  protected Set<Path> listProcessedFiles(Path targetDirectory) {
    try (var fileList = Files.walk(targetDirectory)) {
      return fileList.filter(p -> p.toString().endsWith(".csv_processed"))
          .collect(Collectors.toSet());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
