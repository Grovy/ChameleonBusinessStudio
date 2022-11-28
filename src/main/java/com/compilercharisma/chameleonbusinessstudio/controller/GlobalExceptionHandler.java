package com.compilercharisma.chameleonbusinessstudio.controller;

import com.compilercharisma.chameleonbusinessstudio.dto.HttpErrorMessage;
import com.compilercharisma.chameleonbusinessstudio.exception.*;
import com.twilio.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String RESOURCE_NOT_FOUND_MESSAGE = "error.message.resourceNotFound";
    public static final String MALFORMED_REQUEST_MESSAGE = "error.message.malformedRequest";
    public static final String UNSUPPORTED_OPERATION_MESSAGE = "error.message.unsupported";
    public static final String EXTERNAL_ERROR_MESSAGE = "error.message.externalError";

    @ExceptionHandler(NoUserLoggedInException.class)
    public ResponseEntity<HttpErrorMessage> handleNoLoggedInUser(NoUserLoggedInException ex) {
        log.error(ex.getMessage(), ex);
        var response = HttpErrorMessage.builder()
                .message(ex.getMessage())
                .code(RESOURCE_NOT_FOUND_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotRegisteredException.class)
    public ResponseEntity<HttpErrorMessage> handleUserNotFound(UserNotRegisteredException ex) {
        log.error(ex.getMessage(), ex);
        var response = HttpErrorMessage.builder()
                .message(ex.getMessage())
                .code(RESOURCE_NOT_FOUND_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAppointmentException.class)
    public ResponseEntity<HttpErrorMessage> handleInvalidAppointment(InvalidAppointmentException ex) {
        log.error(ex.getMessage(), ex);
        var response = HttpErrorMessage.builder()
                .message(ex.getMessage())
                .code(MALFORMED_REQUEST_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<HttpErrorMessage> handleExternalError(ExternalServiceException ex) {
        log.error(ex.getMessage(), ex);
        var response = HttpErrorMessage.builder()
                .message(ex.getMessage())
                .code(EXTERNAL_ERROR_MESSAGE)
                .build();
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<HttpErrorMessage> handleExternalError(UnsupportedOperationException ex) {
        log.error(ex.getMessage(), ex);
        var response = HttpErrorMessage.builder()
                .message(ex.getMessage())
                .code(UNSUPPORTED_OPERATION_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppointmentNotFound.class)
    public ResponseEntity<HttpErrorMessage> handleResourceNotFound(AppointmentNotFound ex) {
        log.error(ex.getMessage(), ex);
        var response = HttpErrorMessage.builder()
                .message(ex.getMessage())
                .code(RESOURCE_NOT_FOUND_MESSAGE)
                .build();
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<HttpErrorMessage> handleTwilioNotFoundException(AppointmentNotFound ex) {
        log.error(ex.getMessage(), ex);
        var response = HttpErrorMessage.builder()
                .message(ex.getMessage())
                .code(RESOURCE_NOT_FOUND_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
