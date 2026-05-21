package com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.time.LocalDateTime;

@WorkflowInterface
public interface MissingTCWorkflow {

  @WorkflowMethod
  void process(LocalDateTime fromDate, LocalDateTime toDate);
}
