package com.github.innovationforge.ccl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, HttpServletResponse response, @NonNull Object handler, Exception ex)
            throws Exception {
        int statusCode = response.getStatus();
        if (statusCode < 200 || statusCode >= 300) {
            logRequestDetails((ContentCachingRequestWrapper) request, statusCode);
        }
    }

    private void logRequestDetails(ContentCachingRequestWrapper request, int statusCode) throws IOException {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String requestBody = new String(request.getContentAsByteArray(), request.getCharacterEncoding());

        String curlCommand = "curl -X " + method + " '" + url + "' -H 'Accept: */*'";
        if (requestBody != null && !requestBody.isEmpty()) {
            curlCommand += " -H 'Content-Type: application/json' -d '" + requestBody + "'";
        }

        log.info("Request URL: {}, Method: {}, Status Code: {}", url, method, statusCode);
        log.info("Curl Command: {}", curlCommand);
    }
}