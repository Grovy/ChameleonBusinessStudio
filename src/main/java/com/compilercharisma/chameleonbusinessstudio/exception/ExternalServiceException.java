package com.compilercharisma.chameleonbusinessstudio.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    private String message;
    private HttpStatus httpStatus;

    public ExternalServiceException(String message, HttpStatus httpStatus, Throwable inner){
        super(message, inner);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public ExternalServiceException(String message, HttpStatus httpStatus){
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public ExternalServiceException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }
}
