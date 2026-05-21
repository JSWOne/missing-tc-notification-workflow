package com.jswone.missing_tc_notification_workflow.config;

import com.jswone.missing_tc_notification_workflow.properties.ExternalApiProperties;
import com.jswone.missing_tc_notification_workflow.properties.ServiceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class JomsClientConfig {

  private final ExternalApiProperties properties;

  @Bean
  public RestClient jomsRestClient(RestClient.Builder builder) {
    ServiceConfig joms = properties.getServices().get("joms");
    RestClient.Builder client = builder.baseUrl(joms.getBaseUrl());

    if (StringUtils.hasText(joms.getApiKey())) {
      client.defaultHeader(Constants.X_API_KEY, joms.getApiKey());
    }

    client.defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    return client.build();
  }
}
