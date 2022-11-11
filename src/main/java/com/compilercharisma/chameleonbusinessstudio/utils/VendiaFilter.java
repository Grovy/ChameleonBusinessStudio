package com.compilercharisma.chameleonbusinessstudio.utils;

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
        if("_id".equals(field) || "id".equals(field)){
            throw new IllegalArgumentException("Vendia cannot filter by ID; see the VendiaQueryBuilder documentation");
        }
        VendiaField.validate(field);
        var q = String.format("%s:{eq:\"%s\"}", field, escape(value));
        return new VendiaFilter(q);
    }

    /**
     * @param field the field to filter
     * @param value the value the given field must be greater than or equal to
     * @return a filter matching records whose field is greater than the given
     *  value
     */
    public static VendiaFilter fieldGreaterThanOrEqualTo(String field, String value){
        VendiaField.validate(field);
        var q = String.format("%s:{ge:\"%s\"}", field, escape(value));
        return new VendiaFilter(q);
    }

    /**
     * @param field the field to filter
     * @param value the value the given field must be less than or equal to
     * @return a filter matching records whose field is less than the given
     *  value
     */
    public static VendiaFilter fieldLessThanOrEqualTo(String field, String value){
        VendiaField.validate(field);
        var q = String.format("%s:{le:\"%s\"}", field, escape(value));
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
