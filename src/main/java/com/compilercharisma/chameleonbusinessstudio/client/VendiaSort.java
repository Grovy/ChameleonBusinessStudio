package com.compilercharisma.chameleonbusinessstudio.client;

/**
 * specifies the order a field should be sorted in
 */
public class VendiaSort {
    /**
     * sorts fields in an ascending order
     */
    public static final String ASC = "ASC";

    /**
     * sorts fields in a descending order
     */
    public static final String DESC = "DESC";

    private final VendiaField field;
    private final boolean isAscending;

    private VendiaSort(VendiaField field, boolean isAscending){
        this.field = field;
        this.isAscending = isAscending;
    }

    /**
     * sorts the given field in ascending order
     * 
     * @param field the field to sort
     * @return a specification to sort the given field
     */
    public static VendiaSort by(String field){
        return by(field, true);
    }

    /**
     * sorts the given field in the given order
     * 
     * @param field the field to store
     * @param isAscending true to sort ascending, false for descending order
     * @return a specification to sort the given field
     */
    public static VendiaSort by(String field, boolean isAscending){
        return new VendiaSort(new VendiaField(field), isAscending);
    }

    /**
     * @return this specification, ready for a Vendia query
     */
    @Override
    public String toString(){
        return String.format(
            "%s: %s", 
            field.toString(), 
            isAscending ? ASC : DESC
        );
    }
}
