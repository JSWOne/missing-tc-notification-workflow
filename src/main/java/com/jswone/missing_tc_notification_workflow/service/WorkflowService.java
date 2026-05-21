package com.jswone.missing_tc_notification_workflow.service;

import com.jswone.missing_tc_notification_workflow.dto.WorkflowResponse;
import java.time.LocalDateTime;

public interface WorkflowService {
  WorkflowResponse<?> temporalHealthCheck();

  WorkflowResponse<?> triggerMissingTCWorkflow(LocalDateTime fromDate, LocalDateTime toDate);

  WorkflowResponse<?> ensureDailySchedule();
}
