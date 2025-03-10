package org.example.discovery.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "discovery")
public class DiscoveryProperties {

  private final Report report = new Report();
  private final Housekeeper housekeeper = new Housekeeper();
  private String logPath;

  public String getPath() {
    return this.report.getPath();
  }

  public String getFileName() {
    return this.report.getFileName();
  }

  public boolean isReportEnabled() {
    return this.report.isEnabled();
  }

  public boolean isHouseKeeperEnabled() {
    return this.housekeeper.isEnabled();
  }

  public long getLagMs() {
    return this.report.getLagMs();
  }

  public long getRetentionMinutes() {
    return this.housekeeper.getRetentionMinutes();
  }

  public int getTopCount() {
    return this.report.getTopCount();
  }


  @Data
  public static class Report {

    private String fileName;
    private String path;
    private boolean enabled;
    private int topCount;
    private String cronExpression;
    private long lagMs;
  }

  @Data
  public static class Housekeeper {

    private boolean enabled;
    private int retentionMinutes;
  }
}
