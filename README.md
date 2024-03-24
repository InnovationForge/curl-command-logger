# curl-command-logger

The `curl-command-logger` is a Java application built with Spring Boot and Maven. The main idea behind this application is to enhance the debugging process in production environments.

## Problem Statement

In production applications, logging is typically set to error level to avoid flooding the logs with information. However, when an error occurs (i.e., when the HTTP status code is other than 200), it can be challenging to understand the scenario that led to the error based solely on the error logs.

## Solution

The `curl-command-logger` addresses this problem by logging the details of the request that led to the error. This way, developers can recreate the exact scenario that caused the error, making it easier to understand and fix the issue.

The application uses a `LoggingInterceptor` that intercepts all incoming HTTP requests. If a request results in an error, the interceptor logs the details of the request, including the URL, method, status code, and a curl command that can be used to recreate the request.

## How It Works

The `LoggingInterceptor` class implements the `HandlerInterceptor` interface provided by Spring MVC. It overrides the `preHandle` and `afterCompletion` methods:

- In the `preHandle` method, the incoming `HttpServletRequest` is wrapped with a `ContentCachingRequestWrapper`. This allows the application to read the input stream multiple times, which is necessary to log the request body.

- In the `afterCompletion` method, the HTTP status code of the response is checked. If the status code is not 200, indicating an error, the details of the request are logged.

The `logRequestDetails` method is responsible for constructing the curl command that can be used to recreate the request. It always includes the `-H 'Accept: */*'` header. If the request has a body, it also includes the `-H 'Content-Type: application/json'` header and the request body.

## Note

The `request` object is being wrapped with `ContentCachingRequestWrapper` and re-assigned to the `request` variable, but this new value is never used afterwards in the `preHandle` method. This is because method parameters in Java are passed by value, not by reference. Therefore, re-assigning a new value to the `request` parameter inside the `preHandle` method does not affect the original `request` object outside of this method.

To resolve this issue, the wrapped `request` object is stored in a `ThreadLocal` variable so it can be accessed in the `afterCompletion` method.