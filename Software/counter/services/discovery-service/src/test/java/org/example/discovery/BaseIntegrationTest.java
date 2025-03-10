package org.example.discovery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.example.common.component.TimeDrifter;
import org.example.common.service.CounterStore;
import org.example.discovery.configuration.DiscoveryProperties;
import org.example.discovery.configuration.ScheduledTasks;
import org.example.discovery.service.DiscoveryService;
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
    DiscoveryApplication.class})
@Slf4j
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class BaseIntegrationTest {

  @Autowired
  protected WebTestClient webClient;

  @SpyBean
  protected TimeDrifter timeDrifter;
  @SpyBean
  protected DiscoveryProperties discoveryProperties;
  @SpyBean
  protected DiscoveryService discoveryService;
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

  protected Set<Path> listProcessedFiles(Path targetDirectory) {
    try (var fileList = Files.walk(targetDirectory)) {
      return fileList.filter(p -> p.toString().endsWith(".txt"))
          .collect(Collectors.toSet());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
