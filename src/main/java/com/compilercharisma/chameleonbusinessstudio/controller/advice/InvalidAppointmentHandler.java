package com.compilercharisma.chameleonbusinessstudio.controller.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;
import com.compilercharisma.chameleonbusinessstudio.exception.InvalidAppointmentException;

// https://stackoverflow.com/a/66128060
// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestControllerAdvice.html
@RestControllerAdvice
public class InvalidAppointmentHandler {
    
    private final Logger logger = LoggerFactory.getLogger(InvalidAppointmentHandler.class);

    @ExceptionHandler(InvalidAppointmentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppointmentEntity handleInvalidAppointment(InvalidAppointmentException ex){
        logger.error(ex.getMessage(), ex);
        return ex.getInvalidAppointment();
    }
}
