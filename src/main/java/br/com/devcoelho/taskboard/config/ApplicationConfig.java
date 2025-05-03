package br.com.devcoelho.taskboard.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.TimeZone;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração principal da aplicação FlowDeck. Configura os componentes, repositórios, entidades e
 * transações.
 */
@Configuration
@ComponentScan(basePackages = "br.com.devcoelho.taskboard")
@EntityScan(basePackages = "br.com.devcoelho.taskboard.model")
@EnableTransactionManagement
public class ApplicationConfig {

  /**
   * Configura o ObjectMapper para serialização/deserialização JSON, com suporte adequado para tipos
   * de data/hora do Java 8+.
   *
   * @return ObjectMapper configurado
   */
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.setTimeZone(TimeZone.getDefault());
    return objectMapper;
  }

  /**
   * Configura suporte a internacionalização para mensagens de erro e validação.
   *
   * @return MessageSource configurado
   */
  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasenames("messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  /**
   * Configura CORS (Cross-Origin Resource Sharing) para permitir requisições de diferentes origens,
   * necessário para integração com frontend.
   *
   * @return WebMvcConfigurer configurado
   */
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/api/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .maxAge(3600);
      }
    };
  }
}
