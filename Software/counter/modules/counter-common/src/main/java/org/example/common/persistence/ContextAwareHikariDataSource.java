package org.example.common.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextAwareHikariDataSource extends HikariDataSource {

  public ContextAwareHikariDataSource(HikariConfig configuration) {
    super(configuration);
  }

  @Override
  public Connection getConnection() throws SQLException {
    return super.getConnection();
  }
}
