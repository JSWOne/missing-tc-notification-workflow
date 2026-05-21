package com.jswone.missing_tc_notification_workflow.controller;

import com.jswone.missing_tc_notification_workflow.dto.WorkflowResponse;
import com.jswone.missing_tc_notification_workflow.service.WorkflowService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/missing-tc-notification-workflow")
public class WorkflowController {

  private final WorkflowService workflowService;

  @GetMapping("/health-check")
  public ResponseEntity<WorkflowResponse<?>> healthCheck() {
    return ResponseEntity.ok(workflowService.temporalHealthCheck());
  }

  @PostMapping("/trigger")
  public ResponseEntity<WorkflowResponse<?>> trigger() {
    LocalDate yesterday = LocalDate.now().minusDays(1);
    LocalDateTime from = yesterday.atStartOfDay();
    LocalDateTime to = yesterday.plusDays(1).atStartOfDay().minusSeconds(1);
    log.info("From: {}, To: {}", from, to);
    return ResponseEntity.ok(workflowService.triggerMissingTCWorkflow(from, to));
  }
}
