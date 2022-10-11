package com.gary.backendv2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class HttpException extends HttpStatusCodeException {
    public HttpException(HttpStatus statusCode) {
        super(statusCode);
    }

    public HttpException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }
}
