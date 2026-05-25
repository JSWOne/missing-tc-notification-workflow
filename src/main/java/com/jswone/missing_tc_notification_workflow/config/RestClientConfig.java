package com.jswone.missing_tc_notification_workflow.config;

import com.jswone.missing_tc_notification_workflow.config.interceptor.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

  private final LoggingInterceptor loggingInterceptor;

  @Bean
  public RestClient.Builder restClientBuilder() {
    return RestClient.builder().requestInterceptor(loggingInterceptor);
  }
}
