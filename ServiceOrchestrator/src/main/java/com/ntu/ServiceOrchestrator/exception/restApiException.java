package com.ntu.ServiceOrchestrator.exception;

public class restApiException extends RuntimeException{
    public restApiException(String message) {
        super(message);
    }

    public restApiException(Throwable cause) {
        super(cause);
    }
}
