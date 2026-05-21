package com.jswone.missing_tc_notification_workflow.config;

import io.temporal.client.schedules.ScheduleClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TemporalConfig {

  private final WorkflowServiceStubs workflowServiceStubs;

  @Bean
  ScheduleClient scheduleClient() {
    return ScheduleClient.newInstance(workflowServiceStubs);
  }
}
