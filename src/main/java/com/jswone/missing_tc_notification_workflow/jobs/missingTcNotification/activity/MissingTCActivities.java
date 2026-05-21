package com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.activity;

import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto.MissingTCInvoiceDetails;
import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto.ShipmentMissingTCDTO;
import io.temporal.activity.ActivityInterface;
import java.time.LocalDateTime;
import java.util.List;

@ActivityInterface
public interface MissingTCActivities {

  List<ShipmentMissingTCDTO> fetchShipments(LocalDateTime from, LocalDateTime to);

  void sendNotification(MissingTCInvoiceDetails request);
}
