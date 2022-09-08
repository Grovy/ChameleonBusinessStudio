package com.compilercharisma.chameleonbusinessstudio.repository;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;
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
    
    private final Path folderPath;
    private final Path filePath;
    private Properties cache;
    
    
    public FileSystemWebsiteConfigurationRepository(){
        this(ROOT);
    }
    
    public FileSystemWebsiteConfigurationRepository(Path filePath){
        this.folderPath = filePath;
        this.filePath = Paths.get(filePath.toString(), "config.properties");
        cache = null;
    }
    
    
    @Override
    public Properties load() {
        if(cache == null){ // not loaded yet
            cache = new Properties();
            try(
                    InputStream in = Files.newInputStream(filePath);
            ){
                cache.load(in);
            } catch (IOException ex) {
                Logger.getLogger(FileSystemWebsiteConfigurationRepository.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(String.format("Failed to open \"%s\"", filePath.toString()), ex);
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
        try(
                OutputStream out = Files.newOutputStream(filePath);
        ){
            cache.store(out, null);
        } catch (IOException ex) {
            Logger.getLogger(FileSystemWebsiteConfigurationRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(String.format("Failed to open \"%s\"", filePath.toString()), ex);
        }
    }
}
