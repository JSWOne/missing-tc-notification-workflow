package com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.workflow;

import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto.MissingTCInvoiceDetails;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface MissingTCNotificationWorkflow {

  @WorkflowMethod
  void process(MissingTCInvoiceDetails request);
}
