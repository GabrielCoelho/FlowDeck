package br.com.devcoelho.taskboard.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Configuração para o Jackson, configurando a serialização e desserialização JSON. Esta
 * configuração ajuda a prevenir problemas de serialização circular.
 */
@Configuration
public class JacksonConfig {

  /**
   * Configura o ObjectMapper do Jackson com as configurações adequadas para o projeto.
   *
   * @param builder O builder para construir o ObjectMapper
   * @return Um ObjectMapper configurado
   */
  @Bean
  @Primary
  public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
    ObjectMapper objectMapper = builder.build();

    // Registra o módulo para lidar com tipos de data/hora do Java 8+
    objectMapper.registerModule(new JavaTimeModule());

    // Configurações para evitar problemas de serialização
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // Configuração para não incluir propriedades nulas no JSON
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    return objectMapper;
  }
}
