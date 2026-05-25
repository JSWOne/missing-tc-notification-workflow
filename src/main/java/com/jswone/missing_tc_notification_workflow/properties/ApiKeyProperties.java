package com.jswone.missing_tc_notification_workflow.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security")
public class ApiKeyProperties {
  private String apiKey;
}
