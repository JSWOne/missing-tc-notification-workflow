package com.jswone.missing_tc_notification_workflow.config;

import io.temporal.common.SearchAttributeKey;

public final class ServiceConstants {

  private ServiceConstants() {}

  public static final String X_API_KEY = "x-api-key";

  public static final String TASK_QUEUE = "missing-tc-notification-queue";

  public static final SearchAttributeKey<String> SOURCE_SELLER_EMAIL =
      SearchAttributeKey.forKeyword("sourceSellerEmail");
}
