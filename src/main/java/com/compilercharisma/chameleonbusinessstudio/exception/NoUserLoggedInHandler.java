package com.compilercharisma.chameleonbusinessstudio.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NoUserLoggedInHandler {

    private final Logger logger = LoggerFactory.getLogger(NoUserLoggedInHandler.class);

    @ExceptionHandler(NoUserLoggedInException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleNoLoggedInUser(NoUserLoggedInException ex){
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }
}
