package com.compilercharisma.chameleonbusinessstudio.example;

import javax.persistence.*;

/**
 * This is an example of an ENTITY
 * 
 * entities are data classes that can be translated between Java objects and
 * database records.
 * 
 * @author Matt Crow
 */
@Entity // this means "create a database table for this class"
public class ExampleEntity {
    /*
    fields of an Entity must not be final
    */
    
    @Id // this field will be used as the primary key of the class
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    private String name;
    
    
    /**
     * Entities must have a no-args constructor
     */
    public ExampleEntity(){
        
    }
    
    /*
    Entities must also provide setters and getters for all fields
    note that this is NOT GOOD from an Abstraction & Encapsulation standpoint
    */
    
    public void setId(int id){
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
}
