package com.compilercharisma.chameleonbusinessstudio.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SERVICES are used to incorporate business logic (AKA our code) so we aren't
 * directly acting on out repositories
 * 
 * @author Matt Crow
 */
@Service
public class ExampleService {
    private final ExampleRepository repo;
    
    /**
     * Java Spring sees this ctor and says
     * "this guy needs an ExampleRepositoy. I'll go ahead and make one for him"
     * 
     * @param repo a need this class has, which Spring will supply 
     */
    @Autowired
    public ExampleService(ExampleRepository repo){
        this.repo = repo;
    }
    
    public ExampleEntity createExampleEntity(String name){
        ExampleEntity e = new ExampleEntity();
        e.setName(name);
        
        /*
        This line can do two different things, based upon what it sees in the DB
        table associated with ExampleEntity:
        
        if e's primary key exists within the table,
            update e's representation in the table
        else,
            create a new entity that is the same as e,
            give it an ID,
            store it in the table,
            then return the new entity
        */
        e = repo.save(e);
        
        return e;
    }
}
