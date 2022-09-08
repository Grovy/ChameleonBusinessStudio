package com.compilercharisma.chameleonbusinessstudio.repository;

import java.util.Optional;
import java.util.Properties;

/**
 * Using an interface allows us to decouple classes that require access to the
 * website's configuration from the implementation they use to access it.
 * 
 * We can decide whether we want to do the "IFooRepository & FooRepository"
 * naming convention.
 * 
 * @author Matt Crow
 */
public interface IWebsiteConfigurationRepository {
    /**
     * Use this to access the website configuration properties object.
     * 
     * @return the website configuration properties
     */
    public Properties load();
    
    /**
     * Sets the currently stored properties to be the given properties.
     * 
     * @param newProperties the new values for properties.
     */
    public void store(Properties newProperties);
    
    /**
     * Adds the properties contained in the parameter to the existing 
     * configuration.
     * 
     * @param newProperties the properties to add
     */
    public default void update(Properties newProperties){
        Properties old = load();
        newProperties.stringPropertyNames().forEach(key ->{
            old.setProperty(key, newProperties.getProperty(key));
        });
        store(old);
    }
    
    /**
     * Use this method to get configured properties of the website.
     * 
     * @param property the name of a website configuration property
     * @return an Optional containing the value for the given property. Use its
     *  {@code isPresent} method to check if it is configured, or {@code orElse} 
     *  to provide a default value.
     * 
     * @see java.util.Optional
     */
    public default Optional<String> getValueFor(String property){
        Optional<String> result = Optional.empty();
        Properties props = load();
        if(props.containsKey(property)){
            result = Optional.of(props.getProperty(property));
        }        
        return result;
    }
    
    /**
     * Use this method to set a configured property of the website.
     * 
     * @param property the name of the property to set
     * @param value the value to set the given property to
     */
    public default void setValue(String property, String value){
        Properties props = load();
        props.setProperty(property, value);
        store(props);
    }
}
