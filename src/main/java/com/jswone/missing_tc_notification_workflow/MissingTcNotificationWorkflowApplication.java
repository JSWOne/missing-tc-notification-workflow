package com.jswone.missing_tc_notification_workflow;

import com.jswone.missing_tc_notification_workflow.properties.ExternalApiProperties;
import com.jswone.missing_tc_notification_workflow.properties.MissingTCNotificationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ExternalApiProperties.class, MissingTCNotificationProperties.class})
public class MissingTcNotificationWorkflowApplication {

  public static void main(String[] args) {
    SpringApplication.run(MissingTcNotificationWorkflowApplication.class, args);
  }
}
