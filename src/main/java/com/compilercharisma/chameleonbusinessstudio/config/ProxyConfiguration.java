package com.compilercharisma.chameleonbusinessstudio.config;

import java.util.Properties;

import com.compilercharisma.chameleonbusinessstudio.webconfig.ApplicationFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * used to cache & load the website appearance configuration
 * 
 * uses a .properties file "under the hood" so nothing implodes when properties
 * aren't set
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Component
public class ProxyConfiguration {
    public static final String ORG_NAME = "organization.name";
    public static final String SPLASH_NAME = "splash.filename";
    public static final String LOGO_NAME = "logo.filename";
    public static final String BANNER_COLOR = "banner.color";
    
    private Properties config;
    private final ApplicationFolder folder;
    
    @Autowired
    public ProxyConfiguration(ApplicationFolder folder){
        config = null;
        this.folder = folder;
    }
    
    public boolean isConfigured(){
        return fileExists(); // check for null values in config file?
    }
    
    public void setConfig(Properties config){
        this.config = config;
        save();
    }
    
    public void save(){
        if(config != null){
            writeToFile();
        }
    }
    
    private void writeToFile() {
        folder.saveConfig(config);
    }
    
    public Properties getConfig(){
        if(config == null){
            if(fileExists()){
                readFromFile();
            } else {
                config = new Properties();
            }
        }
        return config;
    }
    
    private boolean fileExists(){
        return folder.fileExists("config.properties");
    }
    
    private void readFromFile(){
        config = folder.readConfig();
    }
}
