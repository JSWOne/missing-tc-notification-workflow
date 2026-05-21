package com.jswone.missing_tc_notification_workflow.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
public class RestClientConfig {

  @Bean
  public RestClient.Builder restClientBuilder() {
    return RestClient.builder()
        .requestInterceptor(
            (request, body, execution) -> {
              log.info("Outgoing request method={} uri={}", request.getMethod(), request.getURI());
              return execution.execute(request, body);
            });
  }
}
