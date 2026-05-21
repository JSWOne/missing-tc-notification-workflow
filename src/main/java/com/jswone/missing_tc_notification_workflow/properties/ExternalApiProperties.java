package com.jswone.missing_tc_notification_workflow.properties;

import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "external-service")
public class ExternalApiProperties {
  private Map<String, ServiceConfig> services;
}
