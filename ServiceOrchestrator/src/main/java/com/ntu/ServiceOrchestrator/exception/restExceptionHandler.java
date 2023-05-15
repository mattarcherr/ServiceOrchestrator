package com.ntu.ServiceOrchestrator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class restExceptionHandler {
    @ExceptionHandler(value = {restApiException.class})
    public ResponseEntity<Object> handleException(restApiException e) {
        restException restException = new restException(
                                        e.getMessage(),
                                        e, HttpStatus.BAD_REQUEST,
                                        ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(restException, HttpStatus.BAD_REQUEST);
    }
}
