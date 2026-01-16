package com.fiap.security;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StructuredLoggingConfig {
  /**
   * Add a custom field to the MDC context
   */
  public void addContext(String key, String value) {
    if (key != null && value != null) {
      MDC.put(key, value);
    }
  }

  /**
   * Add multiple custom fields to the MDC context
   */
  public void addContext(Map<String, String> context) {
    if (context != null) {
      context.forEach(
          (key, value) -> {
            if (key != null && value != null) {
              MDC.put(key, value);
            }
          });
    }
  }

  /**
   * Remove a field from the MDC context
   */
  public void removeContext(String key) {
    MDC.remove(key);
  }

  /**
   * Clear all MDC context
   */
  public void clearContext() {
    MDC.clear();
  }

  /**
   * Add operation context (operation type, entity id, etc.)
   */
  public void addOperationContext(String operationType, String entityId) {
    if (operationType != null) {
      MDC.put("operation.type", operationType);
    }
    if (entityId != null) {
      MDC.put("operation.entity_id", entityId);
    }
  }

  /**
   * Add user context for authenticated operations
   */
  public void addUserContext(String userId, String userEmail) {
    if (userId != null) {
      MDC.put("user.id", userId);
    }
    if (userEmail != null) {
      MDC.put("user.email", userEmail);
    }
  }

  /**
   * Add HTTP request context
   */
  public void addHttpContext(String method, String path, String statusCode) {
    if (method != null) {
      MDC.put("http.method", method);
    }
    if (path != null) {
      MDC.put("http.path", path);
    }
    if (statusCode != null) {
      MDC.put("http.status_code", statusCode);
    }
  }
}
