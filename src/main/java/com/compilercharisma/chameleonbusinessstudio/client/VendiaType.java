package com.compilercharisma.chameleonbusinessstudio.client;

import com.compilercharisma.chameleonbusinessstudio.exception.InvalidVendiaTypeException;

public class VendiaType {
    /**
     * https://graphql-rules.com/rules/naming
     * starts with an uppercase letter,
     * followed by any number of '_', letters, or digits
     */
    private static final String GRAPHQL_TYPE_REGEX = "[A-Z][_0-9A-Za-z]*";
    
    private final String name;

    public VendiaType(String name){
        validate(name);
        this.name = name;
    }

    /**
     * return then name of this type, ready for a Vendia query
     */
    public String toString(){
        return name;
    }

    private static void validate(String name){
        if(!name.matches(GRAPHQL_TYPE_REGEX)){
            throw new InvalidVendiaTypeException(name);
        }
    }
}
