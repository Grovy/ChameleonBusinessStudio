package com.compilercharisma.chameleonbusinessstudio.repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.stereotype.Component;

/**
 * An implementation of IWebsiteConfigurationRepository that uses a file as its
 * backing store. Read from cache, write back on modify.
 * 
 * @author Matt Crow
 */
@Component
public class FileSystemWebsiteConfigurationRepository implements IWebsiteConfigurationRepository {
    private static final Path ROOT = Paths.get(System.getProperty("user.home", "./"), "ChameleonBusinessStudio");
    
    private final Path filePath;
    private Properties cache;
    
    
    public FileSystemWebsiteConfigurationRepository(){
        this(ROOT);
    }
    
    /**
     * @param folderPath path to a folder to load/store config.properties from/to
     */
    public FileSystemWebsiteConfigurationRepository(Path folderPath){
        this.filePath = Paths.get(folderPath.toString(), "config.properties");
        cache = null;
    }
    
    
    @Override
    public Properties load() {
        if(cache == null){ // not loaded yet
            cache = new Properties();
            try(InputStream in = Files.newInputStream(filePath)){
                cache.load(in);
            } catch (IOException ex) {
                throw new RuntimeException(String.format(
                    "Failed to open \"%s\"", 
                    filePath.toString()
                ), ex);
            }
        }
        return cache;
    }

    @Override
    public void store(Properties newProperties) {
        // copy. Properties does not have a copy ctor
        cache = new Properties();
        newProperties.stringPropertyNames().forEach(k->{
            cache.setProperty(k, newProperties.getProperty(k));
        });
        try(OutputStream out = Files.newOutputStream(filePath)){
            cache.store(out, null);
        } catch (IOException ex) {
            throw new RuntimeException(String.format(
                "Failed to open \"%s\"", filePath.toString()
            ), ex);
        }
    }
}
