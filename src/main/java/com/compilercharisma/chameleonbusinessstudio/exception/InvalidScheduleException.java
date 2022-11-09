package com.compilercharisma.chameleonbusinessstudio.exception;

public class InvalidScheduleException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public InvalidScheduleException(String msg) {
        super(msg);
    }

    public InvalidScheduleException() {
        super();
    }
}
