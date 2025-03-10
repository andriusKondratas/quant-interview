package org.example.monitor.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.monitor.service.MonitorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@ConditionalOnProperty(name = "monitor.scan.enabled", havingValue = "true")
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

  private final MonitorService monitorService;
  private final MonitorProperties monitorProperties;

  @Scheduled(cron = "${monitor.scan.cron-expression}")
  public void scan() {
    if (monitorProperties.isScanEnabled()) {
      monitorService.execute();
    }
  }
}
