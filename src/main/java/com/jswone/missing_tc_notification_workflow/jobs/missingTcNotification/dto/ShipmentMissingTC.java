package com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public interface ShipmentMissingTC {
  String getOrderNumber();

  String getInvoiceNumber();

  String getShipmentNumber();

  String getSourceSellerVmId();

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime getInvoiceDate();
}
