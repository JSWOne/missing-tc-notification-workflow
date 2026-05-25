package com.jswone.missing_tc_notification_workflow.config.filter;

import com.jswone.missing_tc_notification_workflow.config.ServiceConstants;
import com.jswone.missing_tc_notification_workflow.properties.ApiKeyProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

  private final ApiKeyProperties properties;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String apiKey = request.getHeader(ServiceConstants.X_API_KEY);
    if (!StringUtils.hasText(apiKey) || !apiKey.equals(properties.getApiKey())) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid API key");
      return;
    }
    filterChain.doFilter(request, response);
  }
}
