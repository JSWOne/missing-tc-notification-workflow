package com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto;

import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentMissingTcDto {
  private String sourceSellerName;
  private String sourceSellerEmail;
  private List<MissingTCDto> orderData;
}
