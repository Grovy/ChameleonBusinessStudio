package com.compilercharisma.chameleonbusinessstudio.client;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Fluent Builder design pattern.
 * Create queries for Vendia's API using an SQL-like syntax.
 */
public class VendiaQueryBuilder {
    private final List<VendiaField> fields = new ArrayList<>();
    private Optional<VendiaFilter> filter = Optional.empty();
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
     * Discards the previous filter, then sets it to the given one.
     * 
     * @param filter the criteria to filter on
     * @return this, for chaining purposes
     */
    public VendiaQueryBuilder where(VendiaFilter filter){
        this.filter = Optional.of(filter);
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
        var f = filter.isPresent() ? filter.get().toString() : "";
        return String.format("filter:{%s}", f);
    }

    /**
     * @return a query that can be sent to the VendiaClient
     */
    public String build(){
        var t = getTypeString();
        var f = getFilterString();
        var fs = getFieldsString();
        return String.format("query{list%s(%s){%s %s}}", t, f, t, fs);
    }
}
