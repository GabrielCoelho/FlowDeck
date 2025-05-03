package br.com.devcoelho.taskboard.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuração de persistência para o FlowDeck. Habilita o gerenciamento de transações e os
 * repositórios JPA. Configura o EntityManager, DataSource e outras propriedades relacionadas a
 * JPA/Hibernate.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "br.com.devcoelho.taskboard.repository")
@PropertySource("classpath:application.properties")
public class PersistenceConfig {

  @Autowired private Environment env;

  /**
   * Configura a fonte de dados (DataSource) para conexão com o banco de dados. As configurações são
   * lidas do arquivo application.properties.
   *
   * @return DataSource configurado
   */
  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(env.getProperty("spring.datasource.url"));
    dataSource.setUsername(env.getProperty("spring.datasource.username"));
    dataSource.setPassword(env.getProperty("spring.datasource.password"));
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    return dataSource;
  }

  /**
   * Configura o EntityManagerFactory que gerencia as entidades JPA.
   *
   * @return LocalContainerEntityManagerFactoryBean configurado
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource());
    em.setPackagesToScan("br.com.devcoelho.taskboard.model");

    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaProperties(additionalProperties());

    return em;
  }

  /**
   * Configura o gerenciador de transações para o JPA.
   *
   * @return PlatformTransactionManager configurado
   */
  @Bean
  public PlatformTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
    return transactionManager;
  }

  /**
   * Configura o processador de tradução de exceções JPA para exceções Spring.
   *
   * @return PersistenceExceptionTranslationPostProcessor configurado
   */
  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }

  /**
   * Configurações adicionais para o Hibernate.
   *
   * @return Properties com as configurações
   */
  private Properties additionalProperties() {
    Properties properties = new Properties();
    properties.setProperty(
        "hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto", "validate"));
    properties.setProperty(
        "hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
    properties.setProperty("hibernate.show_sql", env.getProperty("spring.jpa.show-sql", "true"));
    properties.setProperty(
        "hibernate.format_sql",
        env.getProperty("spring.jpa.properties.hibernate.format_sql", "true"));

    // Configurações de performance
    properties.setProperty("hibernate.jdbc.batch_size", "50");
    properties.setProperty("hibernate.order_inserts", "true");
    properties.setProperty("hibernate.order_updates", "true");
    properties.setProperty("hibernate.jdbc.batch_versioned_data", "true");

    // Configuração para carregar lazy collections quando necessário
    properties.setProperty("hibernate.enable_lazy_load_no_trans", "true");

    return properties;
  }
}
