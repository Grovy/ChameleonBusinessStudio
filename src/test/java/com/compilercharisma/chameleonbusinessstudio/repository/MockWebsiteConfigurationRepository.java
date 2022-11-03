package com.compilercharisma.chameleonbusinessstudio.repository;

import java.util.Properties;

/**
 * Inject this into objects requiring IWebsiteConfigurationRepository so we can
 * test without interacting with the file system.
 * 
 * @author Matt Crow
 */
public class MockWebsiteConfigurationRepository implements WebsiteConfigurationRepository{
    private Properties cache;
    
    
    public MockWebsiteConfigurationRepository(){
        this(new Properties());
    }
    
    public MockWebsiteConfigurationRepository(Properties props){
        cache = props;
    }
    
    
    @Override
    public Properties load() {
        return cache;
    }

    @Override
    public void store(Properties newProperties) {
        newProperties.stringPropertyNames().forEach(key -> {
            cache.setProperty(key, newProperties.getProperty(key));
        });
    }
}
