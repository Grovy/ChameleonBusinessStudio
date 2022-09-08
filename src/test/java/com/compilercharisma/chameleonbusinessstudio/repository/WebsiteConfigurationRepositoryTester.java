package com.compilercharisma.chameleonbusinessstudio.repository;

import java.util.Optional;
import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Matt Crow
 */
public class WebsiteConfigurationRepositoryTester {
    
    private IWebsiteConfigurationRepository makeSut(){
        return makeSut(new Properties());
    }
    
    private IWebsiteConfigurationRepository makeSut(Properties props){
        // we can swap implementations here
        return new MockWebsiteConfigurationRepository(props);
    }
    
    @Test
    public void givenAnEmptyRepository_whenGetValueIsCalled_noResultIsPresent(){
        IWebsiteConfigurationRepository sut = makeSut();
        
        Optional<String> value = sut.getValueFor("foo");
        
        Assertions.assertFalse(value.isPresent());
    }
    
    @Test
    public void givenAConfiguredRepository_whenGetValueIsCalled_aValueIsReturned(){
        String key = "foo";
        String expected = "bar";
        Properties props = new Properties();
        props.setProperty(key, expected);
        IWebsiteConfigurationRepository sut = makeSut(props);
        
        Optional<String> value = sut.getValueFor(key);
        
        Assertions.assertTrue(value.isPresent());
        Assertions.assertEquals(expected, value.get());
    }
    
    @Test
    public void givenAnEmptyRepository_whenSetValueIsCalled_aValueIsSet(){
        String key = "foo";
        String expected = "bar";
        Properties props = new Properties();
        IWebsiteConfigurationRepository sut = makeSut(props);
        
        sut.setValue(key, expected);
        
        Assertions.assertEquals(expected, props.getProperty(key));
    }
    
    @Test
    public void givenAConfiguredRepository_whenSetValueIsCalled_previousValuesAreNotRemoved(){
        String key = "foo";
        String expected = "bar";
        Properties props = new Properties();
        props.setProperty(key, expected);
        IWebsiteConfigurationRepository sut = makeSut(props);
        
        sut.setValue("baz", "qux");
        Optional<String> actual = sut.getValueFor(key);
        
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected, actual.get());
    }
}
