package com.jswone.missing_tc_notification_workflow.client;

import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto.MissingTCInvoiceDetails;
import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.dto.ShipmentMissingTcDto;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class JomsClient {

  private final RestClient jomsRestClient;

  private static final String FETCH_MISSING_TC =
      "/internal/v1/shipment/fetch-shipments-with-missing-tc";

  private static final String SEND_NOTIFICATION =
      "/internal/v1/shipment/send-missing-tc-notification";

  public List<ShipmentMissingTcDto> fetchShipmentsWithMissingTC(
      LocalDateTime fromDate, LocalDateTime toDate) {
    try {
      log.info("Fetching shipments with missing TC from={} to={}", fromDate, toDate);
      List<ShipmentMissingTcDto> response =
          jomsRestClient
              .get()
              .uri(
                  uriBuilder -> {
                    uriBuilder.path(FETCH_MISSING_TC);
                    if (fromDate != null && toDate != null) {
                      uriBuilder.queryParam("fromDate", fromDate);
                      uriBuilder.queryParam("toDate", toDate);
                    }
                    return uriBuilder.build();
                  })
              .retrieve()
              .body(new ParameterizedTypeReference<>() {});
      return Optional.ofNullable(response).orElse(Collections.emptyList());
    } catch (Exception ex) {
      log.error(
          "Failed to fetch missing TC shipments from={} to={}, reason={}",
          fromDate,
          toDate,
          ex.getMessage());
      return Collections.emptyList();
    }
  }

  public void sendMissingTCNotification(MissingTCInvoiceDetails request) {
    if (request == null || !StringUtils.hasText(request.getSourceSellerEmail())) {
      log.warn("Invalid notification request - {}", request);
      return;
    }

    try {
      log.info("Sending Missing TC notification ={}", request);
      jomsRestClient.post().uri(SEND_NOTIFICATION).body(request).retrieve().toBodilessEntity();
      log.info("Missing TC notification sent seller={}", request.getSourceSellerEmail());
    } catch (Exception ex) {
      log.error(
          "Failed sending Missing TC notification seller={}, reason={}",
          request.getSourceSellerEmail(),
          ex.getMessage());
    }
  }
}
