package com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.workflow;

import com.jswone.missing_tc_notification_workflow.config.ServiceConstants;
import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.activity.MissingTCActivities;
import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto.MissingTCInvoiceDetails;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import org.slf4j.Logger;

@WorkflowImpl(workers = ServiceConstants.TASK_QUEUE)
public class MissingTCNotificationWorkflowImpl implements MissingTCNotificationWorkflow {

  private static final Logger logger = Workflow.getLogger(MissingTCNotificationWorkflowImpl.class);

  private final RetryOptions retryOptions =
      RetryOptions.newBuilder()
          .setInitialInterval(Duration.ofSeconds(5))
          .setMaximumAttempts(5)
          .build();

  private final ActivityOptions activityOptions =
      ActivityOptions.newBuilder()
          .setStartToCloseTimeout(Duration.ofMinutes(1))
          .setRetryOptions(retryOptions)
          .build();

  private final MissingTCActivities activities =
      Workflow.newActivityStub(MissingTCActivities.class, activityOptions);

  @Override
  public void process(MissingTCInvoiceDetails missingTCInvoiceDetails) {
    logger.info("Sending notification = {}", missingTCInvoiceDetails);

    String sourceSellerEmail = missingTCInvoiceDetails.getSourceSellerEmail();
    Workflow.upsertTypedSearchAttributes(
        ServiceConstants.SOURCE_SELLER_EMAIL.valueSet(sourceSellerEmail));

    activities.sendNotification(missingTCInvoiceDetails);
  }
}
