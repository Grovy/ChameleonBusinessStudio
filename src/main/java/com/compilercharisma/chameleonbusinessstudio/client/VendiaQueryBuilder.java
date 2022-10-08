package com.compilercharisma.chameleonbusinessstudio.client;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Fluent Builder design pattern.
 * Create queries for Vendia's API using an SQL-like syntax.
 */
public class VendiaQueryBuilder {
    private final List<VendiaField> fields = new ArrayList<>();
    private final List<VendiaFilter> filters = new ArrayList<>();
    private final List<VendiaSort> sorts = new ArrayList<>();
    private VendiaType type = null;
    
    /**
     * Removes any currently selected fields, then selects the given ones.
     * 
     * @param fields the list of fields to include in the query
     * @return this, for chaining purposes
     */
    public VendiaQueryBuilder select(String... fields){
        this.fields.clear();
        for(var col : fields){
            VendiaField.validate(col);
            this.fields.add(new VendiaField(col));
        }
        return this;
    }

    /**
     * Discards the previous type to select from, then sets it to the given one
     * 
     * @param type the name of the type to query
     * @return this, for chaining purposes
     */
    public VendiaQueryBuilder from(String type){
        this.type = new VendiaType(type);
        return this;
    }

    /**
     * Discards the previous filters, then sets it to the given ones.
     * 
     * @param filter the criteria to filter on
     * @return this, for chaining purposes
     */
    public VendiaQueryBuilder where(VendiaFilter... filters){
        this.filters.clear();
        for(var filter : filters) {
            this.filters.add(filter);
        }
        return this;
    }

    /**
     * Discards any previous sorts, then sets it to the given ones.
     * 
     * @param sorts the criteria to sort on
     * @return this, for chaining purposes
     */
    public VendiaQueryBuilder orderBy(VendiaSort... sorts){
        this.sorts.clear();
        for(var sort : sorts){
            this.sorts.add(sort);
        }
        return this;
    }

    /**
     * Discards any previous sorts, then sets it to the given ones.
     * 
     * @param sorts the criteria to sort on
     * @return this, for chaining purposes
     */
    public VendiaQueryBuilder orderBy(List<VendiaSort> sorts){
        this.sorts.clear();
        for(var sort : sorts){
            this.sorts.add(sort);
        }
        return this;
    }

    /**
     * @return the fields part of a query
     */
    public String getFieldsString(){
        var inner = fields.stream()
            .map(VendiaField::toString)
            .collect(Collectors.joining(","));
        return String.format("{%s}", inner);
    }

    /**
     * @return the type part of the query
     */
    public String getTypeString(){
        if(type == null){
            throw new UnsupportedOperationException("cannot get type string before from(type) has been called");
        }
        return String.format("_%sItems", type.toString());
    }

    public String getFilterString(){
        var f = filters.stream()
            .map(VendiaFilter::toString)
            .collect(Collectors.joining(","));
        return String.format("filter:{%s}", f);
    }

    public String getSortString(){
        if(!isSorted()){
            // Vendia cannot accept empty order, but can accept empty filter
            return "";
        }
        var s = sorts.stream()
            .map(VendiaSort::toString)
            .collect(Collectors.joining(","));
        return String.format("order:{%s}", s);
    }

    private boolean isSorted(){
        return !sorts.isEmpty();
    }

    /**
     * @return a query that can be sent to the VendiaClient
     */
    public String build(){
        var criteria = getFilterString();
        if(isSorted()){
            criteria += "," + getSortString();
        }
        var t = getTypeString();
        var fs = getFieldsString();
        return String.format("query{list%s(%s){%s %s}}", t, criteria, t, fs);
    }
}
