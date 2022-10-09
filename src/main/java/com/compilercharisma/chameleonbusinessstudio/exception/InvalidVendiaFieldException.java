package com.compilercharisma.chameleonbusinessstudio.exception;

/**
 * This exception is thrown upon attempting to build a Vendia query with an
 * invalid field name. This helps avoid injection attacks.
 */
public class InvalidVendiaFieldException extends RuntimeException {
    private final String invalidField;

    /**
     * @param invalidField the invalid field that triggered this exception
     */
    public InvalidVendiaFieldException(String invalidField){
        super(String.format("Invalid Vendia column: \"%s\"", invalidField));
        this.invalidField = invalidField;
    }

    /**
     * @return the invalid column that triggered this exception
     */
    public String getInvalidField(){
        return invalidField;
    }
}
