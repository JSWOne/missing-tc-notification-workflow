package com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.activity;

import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto.MissingTCInvoiceDetails;
import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto.ShipmentMissingTCDTO;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.time.LocalDateTime;
import java.util.List;

@ActivityInterface
public interface MissingTCActivities {

  @ActivityMethod
  List<ShipmentMissingTCDTO> fetchShipments(LocalDateTime from, LocalDateTime to);

  @ActivityMethod
  void sendNotification(MissingTCInvoiceDetails request);
}
