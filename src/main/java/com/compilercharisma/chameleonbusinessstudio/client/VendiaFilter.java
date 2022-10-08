package com.compilercharisma.chameleonbusinessstudio.client;

/**
 * specifies a filtering condition to apply to a Vendia query
 */
public class VendiaFilter {
    private final String query;

    private VendiaFilter(String query){
        this.query = query;
    }

    /**
     * only use this for array fields
     *  
     * @param field the field to filter
     * @param value the value that must be in the given field
     * @return a filter matching an array field with the given name that 
     *  include the given value
     */
    public static VendiaFilter fieldContains(String field, String value){
        VendiaField.validate(field);
        var q = String.format("%s:{contains:\"%s\"}", field, escape(value));
        return new VendiaFilter(q);
    }

    /**
     * @param field the field to filter
     * @param value the value the given field must have
     * @return a filter matching the given field with the given value
     */
    public static VendiaFilter fieldEquals(String field, String value){
        VendiaField.validate(field);
        var q = String.format("%s:{eq:\"%s\"}", field, escape(value));
        return new VendiaFilter(q);
    }

    private static String escape(String in){
        return in.replaceAll("\"", "\\\""); //escape quotes 
    }

    /**
     * @return this specification, ready for a Vendia query
     */
    @Override
    public String toString(){
        return query;
    }
}
