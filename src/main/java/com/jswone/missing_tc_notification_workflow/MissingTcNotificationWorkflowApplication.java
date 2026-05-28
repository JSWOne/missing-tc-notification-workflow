package com.jswone.missing_tc_notification_workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MissingTcNotificationWorkflowApplication {

  public static void main(String[] args) {
    SpringApplication.run(MissingTcNotificationWorkflowApplication.class, args);
  }
}
