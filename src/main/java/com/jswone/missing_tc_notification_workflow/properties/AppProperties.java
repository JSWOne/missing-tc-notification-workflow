package com.jswone.missing_tc_notification_workflow.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "temporal.missing-tc-notification")
public class AppProperties {
  private String taskQueue;

  private int startToCloseTimeoutSeconds;
  private int scheduleToCloseTimeoutSeconds;

  private int initialIntervalSeconds;
  private double backoffCoefficient;
  private int maximumAttempts;

  private String scheduleId;
  private String scheduleTimezone;
  private int scheduleHour;
  private int scheduleMinute;
}
