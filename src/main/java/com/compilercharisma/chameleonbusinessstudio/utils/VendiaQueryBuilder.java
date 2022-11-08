package com.compilercharisma.chameleonbusinessstudio.utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Fluent Builder design pattern.
 * Create queries for Vendia's API using an SQL-like syntax.
 * 
 * One consideration: Vendia does not support having the _id field in a list
 * query, so be sure to use withId and limitOne when trying to get a single
 * entity.
 */
public class VendiaQueryBuilder {
    private final List<VendiaField> fields = new ArrayList<>();
    private final List<VendiaFilter> filters = new ArrayList<>();
    private final List<VendiaSort> sorts = new ArrayList<>();
    private VendiaType type = null;
    private boolean single = false;
    private String id = null;
    
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
     * Filters to only include the entity with the given ID
     * 
     * @param id an entity's ID in Vendia 
     * @return this, for chaining purposes
     */
    public VendiaQueryBuilder withId(String id){
        this.id = id;
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
     * Use this to select only a single matching entity, such as when getting an
     * entity by ID or some other unique field.
     * 
     * @return this, for chaining purposes
     */
    public VendiaQueryBuilder limitOne(){
        single = true;
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
        return type.toString();
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
        if(single){
            return buildSingle();
        }

        var criteria = getFilterString();
        if(isSorted()){
            criteria += "," + getSortString();
        }
        var t = getTypeString();
        var fs = getFieldsString();
        return String.format("query{list_%sItems(%s){%s %s}}", t, criteria, t, fs);
    }

    private String buildSingle(){
        var t = getTypeString();
        var fs = getFieldsString();
        return "query{get_%s(id: \"%s\")%s}".formatted(t, id, fs);
    }
}
