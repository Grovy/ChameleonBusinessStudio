package com.compilercharisma.chameleonbusinessstudio.example;

import java.util.Iterator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * A CrudReposity is used to store Entity classes.
 * Note that this is an INTERFACE, so we don't do any definition, just 
 * DECLARATION
 * Spring will read this interface, look at the database type we're using, then
 * create its own IMPLEMENTATION of this interface
 * 
 * CrudRepository<EntityType, PrimaryKeyType>
 * 
 * @author Matt Crow
 */
@Repository
public interface ExampleRepository extends CrudRepository<ExampleEntity, Integer>{
    /**
     * Spring will automatically create a query based on the name of this method
     * pretty cool, huh?
     * 
     * @param name the name of the example entity to get
     * @return the entity with the given name
     */
    public Iterator<ExampleEntity> findAllByName(String name);
}
