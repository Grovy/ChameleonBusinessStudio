package com.compilercharisma.chameleonbusinessstudio.repository;

import java.util.Optional;
import java.util.Properties;

/**
 * Using an interface allows us to decouple classes that require access to the
 * website's configuration from the implementation they use to access it.
 * 
 * @author Matt Crow
 */
public interface WebsiteConfigurationRepository {
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
     * Checks if a website configuration property has been set.
     * 
     * @param property the property to check
     * @return whether the property has been configured
     */
    public default boolean isConfigured(String property){
        return getValueFor(property).isPresent();
    }
    
    /**
     * Use this method to set a configured property of the website.
     * 
     * @param property the name of the property to set
     * @param value the value to set the given property to
     */
    public default void set(String property, String value){
        Properties props = load();
        props.setProperty(property, value);
        store(props);
    }

    /**
     * Gets the value of the property with the given value, throwing an 
     * exception if no such property exists.
     */
    public default String get(String property){
        var props = load();
        if(!props.containsKey(property)){
            throw new RuntimeException("Key not configured: " + property);
        }
        return props.getProperty(property);
    }

    /**
     * Gets the value for the given property, or the given defaultValue if it is
     * not configured.
     * 
     * @param property the name of the property to get
     * @param defaultValue the value to use if the given property is not set
     * 
     * @return the value for the given property or defaultValue if it is not set
     */
    public default String get(String property, String defaultValue){
        var props = load();
        return props.getProperty(property, defaultValue);
    }
}
