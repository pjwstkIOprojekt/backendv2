package com.gary.backendv2.exception.handler;

import com.gary.backendv2.exception.HttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class HttpExceptionHandler {
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Object> handleHttpException(HttpException exception) {
        Map<String, Object> errors = new LinkedHashMap<>();
        errors.put("message", exception.getMessage());
        errors.put("status", exception.getStatusCode());

        return new ResponseEntity<>(errors, (HttpStatus)errors.get("status"));
    }
}
