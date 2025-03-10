package org.example.discovery.configuration;

import com.zaxxer.hikari.HikariConfig;
import java.util.Objects;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.example.common.persistence.ContextAwareHikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = {"org.example"},
    entityManagerFactoryRef = "discoveryEntityManagerFactory",
    transactionManagerRef = "discoveryTransactionManager")
@RequiredArgsConstructor
public class DiscoveryJpaConfig {

  private final Environment env;

  @Bean
  @ConfigurationProperties("datasource.discovery")
  public Properties discoveryDataSourceProperties() {
    return new Properties();
  }

  @Bean
  public DataSource discoveryDataSource() {
    return new ContextAwareHikariDataSource(new HikariConfig(discoveryDataSourceProperties()));
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean discoveryEntityManagerFactory() {
    var em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(discoveryDataSource());
    em.setPackagesToScan("org.example");

    var vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaProperties(hibernateProperties());

    return em;
  }

  @Bean
  public PlatformTransactionManager discoveryTransactionManager(
      @Qualifier("discoveryEntityManagerFactory") LocalContainerEntityManagerFactoryBean discoveryEntityManagerFactory) {
    return new JpaTransactionManager(Objects.requireNonNull(discoveryEntityManagerFactory.getObject()));
  }

  private Properties hibernateProperties() {
    Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", env.getRequiredProperty("datasource.hibernate.discovery.ddl"));
    properties.setProperty("hibernate.dialect", env.getRequiredProperty("datasource.hibernate.discovery.dialect"));
    properties.setProperty("hibernate.default_schema", env.getRequiredProperty("datasource.discovery.schema"));
    properties.setProperty("hibernate.show_sql", env.getRequiredProperty("datasource.hibernate.discovery.showSQL"));

    properties.setProperty("hibernate.order_inserts", "true");
    properties.setProperty("hibernate.jdbc.batch_size", "100000");
    properties.setProperty("hibernate.order_updates", "true");
    properties.setProperty("hibernate.batch_versioned_data", "true");

    return properties;
  }
}
