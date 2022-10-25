package com.compilercharisma.chameleonbusinessstudio.exception;

public class InvalidVendiaTypeException extends RuntimeException {
    public static final long serialVersionUID = 1L;
    
    private final String invalidType;

    public InvalidVendiaTypeException(String invalidType){
        super(String.format("Invalid Vendia type: \"%s\"", invalidType));
        this.invalidType = invalidType;
    }

    public String getInvalidType(){
        return invalidType;
    }
}
