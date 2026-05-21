package com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TCInvoiceDetails {
  private String orderId;
  private String invoiceNumber;
  private String invoiceDate;
}
