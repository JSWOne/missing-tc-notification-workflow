package com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.activity;

import com.jswone.missing_tc_notification_workflow.client.JomsClient;
import com.jswone.missing_tc_notification_workflow.config.ServiceConstants;
import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto.MissingTCInvoiceDetails;
import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto.ShipmentMissingTcDto;
import io.temporal.spring.boot.ActivityImpl;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ActivityImpl(workers = ServiceConstants.TASK_QUEUE)
public class MissingTCActivitiesImpl implements MissingTCActivities {

  private final JomsClient jomsClient;

  @Override
  public List<ShipmentMissingTcDto> fetchShipments(LocalDateTime from, LocalDateTime to) {
    return jomsClient.fetchShipmentsWithMissingTC(from, to);
  }

  @Override
  public void sendNotification(MissingTCInvoiceDetails missingTCInvoiceDetails) {
    jomsClient.sendMissingTCNotification(missingTCInvoiceDetails);
  }
}
