package com.compilercharisma.chameleonbusinessstudio.client;

public class VendiaFilter {
    private final String query;

    private VendiaFilter(String query){
        this.query = query;
    }

    public static VendiaFilter fieldContains(String field, String value){
        VendiaField.validate(field);
        value = value.replaceAll("\"", "\\\""); //escape quotes 
        var q = String.format("%s:{contains:\"%s\"}", field, value);
        return new VendiaFilter(q);
    }

    @Override
    public String toString(){
        return query;
    }
}
