package com.jswone.missing_tc_notification_workflow.config.interceptor;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

    long start = System.currentTimeMillis();

    log.info("Calling {} {}", request.getMethod(), request.getURI());

    ClientHttpResponse response = execution.execute(request, body);

    log.info(
        "{} {} completed with status={} in {}ms",
        request.getMethod(),
        request.getURI(),
        response.getStatusCode(),
        System.currentTimeMillis() - start);

    return response;
  }
}
