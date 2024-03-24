package com.github.innovationforge.ccl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        int statusCode = response.getStatus();
        if (statusCode < 200 || statusCode >= 300) {
            logRequestDetails(request, statusCode);
        }
    }

    private void logRequestDetails(HttpServletRequest request, int statusCode) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String curlCommand = "curl -X " + method + " '" + url + "'";
        log.info("Request URL: {}, Method: {}, Status Code: {}", url, method, statusCode);
        log.info("Curl Command: {}", curlCommand);
    }
}