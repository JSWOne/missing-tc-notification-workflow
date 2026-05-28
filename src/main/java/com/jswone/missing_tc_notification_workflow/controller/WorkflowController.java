package com.jswone.missing_tc_notification_workflow.controller;

import com.jswone.missing_tc_notification_workflow.dto.WorkflowResponse;
import com.jswone.missing_tc_notification_workflow.service.WorkflowService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WorkflowController {

  private final WorkflowService workflowService;

  @GetMapping("/health-check")
  public ResponseEntity<WorkflowResponse<?>> healthCheck() {
    return ResponseEntity.ok(workflowService.temporalHealthCheck());
  }

  @PostMapping("/schedule/ensure")
  public ResponseEntity<WorkflowResponse<?>> ensureSchedule() {
    return ResponseEntity.ok(workflowService.ensureDailySchedule());
  }

  @PostMapping("/trigger")
  public ResponseEntity<WorkflowResponse<?>> trigger(
      @RequestParam(required = false) LocalDateTime from,
      @RequestParam(required = false) LocalDateTime to) {
    if (from == null || to == null) {
      log.info("From or To not provided, defaulting to yesterday's date");
      LocalDate yesterday = LocalDate.now().minusDays(1);
      from = yesterday.atStartOfDay();
      to = yesterday.plusDays(1).atStartOfDay().minusSeconds(1);
    }
    log.info("From: {}, To: {}", from, to);
    return ResponseEntity.ok(workflowService.triggerMissingTCWorkflow(from, to));
  }
}
