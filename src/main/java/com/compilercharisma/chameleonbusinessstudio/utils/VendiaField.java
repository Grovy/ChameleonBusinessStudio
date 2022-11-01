package com.compilercharisma.chameleonbusinessstudio.utils;

import com.compilercharisma.chameleonbusinessstudio.exception.InvalidVendiaFieldException;

/**
 * the field of an object in Vendia
 */
public class VendiaField {
    /**
     * https://graphql-rules.com/rules/naming
     * starts with an '_' or a letter,
     * followed by any number of '_', letters, or digits
     */
    private static final String GRAPHQL_FIELD_REGEX = "[_A-Za-z][_0-9A-Za-z]*";
    private final String name;

    /**
     * @param name the name of this field as it appears in Vendia
     */
    public VendiaField(String name){
        validate(name);
        this.name = name;
    }

    /**
     * this method should only be used on array fields
     * 
     * @param value the value to search for
     * @return a filter that evaluates to true when this field has the given
     *  value as an element
     */
    public VendiaFilter contains(String value){
        return VendiaFilter.fieldContains(name, value);
    }

    public VendiaFilter eq(Object value){
        return VendiaFilter.fieldEquals(name, value.toString());
    }

    public VendiaFilter ge(Object value){
        return VendiaFilter.fieldGreaterThanOrEqualTo(name, value.toString());
    }

    public VendiaFilter le(Object value){
        return VendiaFilter.fieldLessThanOrEqualTo(name, value.toString());
    }

    /**
     * Throws an InvalidVendiaFieldException if the given name cannot be used as
     *  a field name
     * 
     * @param fieldName the name to validate
     */
    public static void validate(String fieldName){
        if(!fieldName.matches(GRAPHQL_FIELD_REGEX)){
            throw new InvalidVendiaFieldException(fieldName);
        }
    }

    /**
     * @return this field as a string, ready to put in a query
     */
    @Override
    public String toString(){
        return name;
    }
}
