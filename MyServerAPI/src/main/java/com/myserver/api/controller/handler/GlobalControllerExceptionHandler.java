package com.myserver.api.controller.handler;

import com.myserver.api.dto.response.MyServerResponse;
import com.myserver.api.exception.NotAuthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<Object> handleException(RuntimeException exception, WebRequest request) {
        final String message = exception.getMessage();
        final int status = HttpStatus.CONFLICT.value();
        final MyServerResponse response = new MyServerResponse(status, message);
        return handleExceptionInternal(exception, response, new HttpHeaders(), HttpStatus.OK, request);
    }

    @ExceptionHandler(value = NotAuthorizedException.class)
    protected ResponseEntity<Object> handleAuthException(RuntimeException exception, WebRequest request) {
        final String message = exception.getMessage();
        final int status = HttpStatus.UNAUTHORIZED.value();
        final MyServerResponse response = new MyServerResponse(status, message);
        return handleExceptionInternal(exception, response, new HttpHeaders(), HttpStatus.OK, request);
    }
}
