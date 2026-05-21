package com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.workflow;

import com.jswone.missing_tc_notification_workflow.config.Constants;
import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.activity.MissingTCActivities;
import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto.MissingTCInvoiceDetails;
import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto.ShipmentMissingTCDTO;
import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto.TCInvoiceDetails;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Async;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

@WorkflowImpl(taskQueues = "missing-tc-notification-queue")
public class MissingTCWorkflowImpl implements MissingTCWorkflow {

  private static final Logger logger = Workflow.getLogger(MissingTCWorkflowImpl.class);

  private final RetryOptions retryOptions = RetryOptions.newBuilder().setMaximumAttempts(3).build();

  private final ActivityOptions activityOptions =
      ActivityOptions.newBuilder()
          .setStartToCloseTimeout(Duration.ofMinutes(2))
          .setRetryOptions(retryOptions)
          .build();

  private final MissingTCActivities activities =
      Workflow.newActivityStub(MissingTCActivities.class, activityOptions);

  @Override
  public void process(LocalDateTime fromDate, LocalDateTime toDate) {
    List<ShipmentMissingTCDTO> shipments = activities.fetchShipments(fromDate, toDate);

    if (shipments.isEmpty()) {
      logger.info("No shipments found");
      return;
    }

    List<Promise<Void>> childPromises = new ArrayList<>();

    for (ShipmentMissingTCDTO shipment : shipments) {
      MissingTCNotificationWorkflow child =
          Workflow.newChildWorkflowStub(
              MissingTCNotificationWorkflow.class,
              ChildWorkflowOptions.newBuilder().setWorkflowId(createWorkflowId(shipment)).build());

      Promise<Void> promise = Async.procedure(child::process, convert(shipment));
      childPromises.add(promise);
    }

    Promise.allOf(childPromises).get();
  }

  private String createWorkflowId(ShipmentMissingTCDTO dto) {
    return "missing-tc-" + dto.getSourceSellerEmail() + "-" + Workflow.randomUUID();
  }

  private MissingTCInvoiceDetails convert(ShipmentMissingTCDTO dto) {
    MissingTCInvoiceDetails response = new MissingTCInvoiceDetails();

    response.setSourceSellerName(dto.getSourceSellerName());
    response.setSourceSellerEmail(dto.getSourceSellerEmail());

    List<TCInvoiceDetails> invoices =
        dto.getOrderData().stream()
            .map(
                order -> {
                  TCInvoiceDetails item = new TCInvoiceDetails();

                  item.setOrderId(order.getOrderNumber());
                  item.setInvoiceNumber(order.getInvoiceNumber());
                  item.setInvoiceDate(order.getInvoiceDate().toString());

                  return item;
                })
            .toList();

    response.setOrderData(invoices);

    return response;
  }
}
