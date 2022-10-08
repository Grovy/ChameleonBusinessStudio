package com.compilercharisma.chameleonbusinessstudio.exception;

public class InvalidScheduleException extends RuntimeException {
    
    public InvalidScheduleException(String msg){
        super(msg);
    }

    public InvalidScheduleException(){
        super();
    }
}
