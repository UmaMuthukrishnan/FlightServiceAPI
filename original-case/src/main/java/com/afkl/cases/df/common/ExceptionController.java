package com.afkl.cases.df.common;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Throwable.class)
    public HttpEntity<String> handleGlobalException(Throwable t) {
        return new ResponseEntity<>(SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public HttpEntity<String> handleGlobalException(HttpServerErrorException e) {
        return new ResponseEntity<>(e.getStatusCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public HttpEntity<String> handleBadRequest() {
        return new ResponseEntity<>(BAD_REQUEST);
    }

}
