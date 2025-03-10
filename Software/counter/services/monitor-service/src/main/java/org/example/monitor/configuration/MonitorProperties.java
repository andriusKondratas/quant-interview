package org.example.monitor.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "monitor")
public class MonitorProperties {

  private final Scan scan = new Scan();
  private String logPath;

  public String getSourcePath() {
    return this.scan.getSourcePath();
  }

  public boolean isScanEnabled() {
    return this.scan.isEnabled();
  }

  public int getFileBatchLimit() {
    return this.scan.getFileBatchLimit();
  }

  @Data
  public static class Scan {
    private String sourcePath;
    private boolean enabled;
    private String cronExpression;
    private int fileBatchLimit;
  }
}
