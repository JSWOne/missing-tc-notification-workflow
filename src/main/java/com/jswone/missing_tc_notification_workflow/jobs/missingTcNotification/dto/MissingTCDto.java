package com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissingTCDto {
  private String orderNumber;
  private String invoiceNumber;
  private String shipmentNumber;
  private String sourceSellerVmId;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime invoiceDate;
}
