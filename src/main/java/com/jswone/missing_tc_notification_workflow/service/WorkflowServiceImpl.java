package com.jswone.missing_tc_notification_workflow.service;

import com.jswone.missing_tc_notification_workflow.dto.WorkflowResponse;
import com.jswone.missing_tc_notification_workflow.jobs.missingTcNotification.workflow.MissingTCWorkflow;
import com.jswone.missing_tc_notification_workflow.properties.AppProperties;
import io.grpc.health.v1.HealthCheckResponse;
import io.temporal.api.enums.v1.ScheduleOverlapPolicy;
import io.temporal.api.enums.v1.WorkflowIdReusePolicy;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.schedules.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowServiceImpl implements WorkflowService {

  private final WorkflowClient workflowClient;
  private final ScheduleClient scheduleClient;
  private final AppProperties props;

  @Override
  public WorkflowResponse<?> temporalHealthCheck() {
    WorkflowResponse<Map<String, String>> response = new WorkflowResponse<>();
    try {
      HealthCheckResponse hc = workflowClient.getWorkflowServiceStubs().healthCheck();
      Map<String, String> data = new HashMap<>();
      data.put("status", hc.getStatus().name());
      response.setIsSuccess(true);
      response.setData(data);
    } catch (Exception e) {
      response.setIsSuccess(false);
      response.setMessage(e.getMessage());
    }
    return response;
  }

  @Override
  public WorkflowResponse<?> triggerMissingTCWorkflow(
      LocalDateTime fromDate, LocalDateTime toDate) {
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
    String workFlowIdSuffix = LocalDateTime.now(ZoneId.of("Asia/Kolkata")).format(FORMATTER);

    WorkflowOptions workflowOptions =
        WorkflowOptions.newBuilder()
            .setTaskQueue(props.getTaskQueue())
            .setWorkflowId("missing-tc-" + workFlowIdSuffix)
            .setWorkflowIdReusePolicy(
                WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE)
            .build();

    MissingTCWorkflow workflow =
        workflowClient.newWorkflowStub(MissingTCWorkflow.class, workflowOptions);

    WorkflowClient.start(workflow::process, fromDate, toDate);

    return WorkflowResponse.builder()
        .isSuccess(true)
        .message("Missing TC Notification Workflow started successfully")
        .build();
  }

  @Override
  public WorkflowResponse<?> ensureDailySchedule() {
    String scheduleId = props.getScheduleId();
    try {
      WorkflowOptions workflowOptions =
          WorkflowOptions.newBuilder()
              .setTaskQueue(props.getTaskQueue())
              .setWorkflowId("missing-tc-daily")
              .build();

      ScheduleActionStartWorkflow action =
          ScheduleActionStartWorkflow.newBuilder()
              .setWorkflowType(MissingTCWorkflow.class)
              .setOptions(workflowOptions)
              .build();

      ScheduleCalendarSpec calendar =
          ScheduleCalendarSpec.newBuilder()
              .setHour(List.of(new ScheduleRange(props.getScheduleHour())))
              .setMinutes(List.of(new ScheduleRange(props.getScheduleMinute())))
              .build();

      ScheduleSpec spec =
          ScheduleSpec.newBuilder()
              .setTimeZoneName(props.getScheduleTimezone())
              .setCalendars(List.of(calendar))
              .build();

      SchedulePolicy policy =
          SchedulePolicy.newBuilder()
              .setOverlap(ScheduleOverlapPolicy.SCHEDULE_OVERLAP_POLICY_SKIP)
              .build();

      Schedule schedule =
          Schedule.newBuilder().setAction(action).setSpec(spec).setPolicy(policy).build();

      scheduleClient.createSchedule(scheduleId, schedule, ScheduleOptions.newBuilder().build());

      return success(scheduleId, "CREATED");
    } catch (ScheduleException e) {
      String msg = Optional.ofNullable(e.getMessage()).orElse("").toLowerCase();
      if (msg.contains("already exists") || msg.contains("exists")) {
        return success(scheduleId, "ALREADY_EXISTS");
      }
      log.error("Failed creating schedule {} - {}", scheduleId, e.getMessage());
      return failure(e.getMessage());
    }
  }

  private WorkflowResponse<?> success(String scheduleId, String status) {
    Map<String, Object> data = Map.of("scheduleId", scheduleId, "status", status);

    WorkflowResponse<Map<String, Object>> response = new WorkflowResponse<>();
    response.setIsSuccess(true);
    response.setMessage(status);
    response.setData(data);

    return response;
  }

  private WorkflowResponse<?> failure(String message) {
    WorkflowResponse<?> response = new WorkflowResponse<>();
    response.setIsSuccess(false);
    response.setMessage(message);
    return response;
  }
}
