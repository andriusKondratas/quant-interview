package org.example.discovery.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.discovery.service.DiscoveryService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@ConditionalOnProperty(name = "discovery.report.enabled", havingValue = "true")
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

  private final DiscoveryService discoveryService;
  private final DiscoveryProperties discoveryProperties;

  @Scheduled(cron = "${discovery.report.cron-expression}")
  public void scan() {
    if (discoveryProperties.isReportEnabled()) {
      discoveryService.execute();
    }
  }
}
