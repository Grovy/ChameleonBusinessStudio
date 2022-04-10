package com.compilercharisma.chameleonbusinessstudio.webconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * used to cache & load the website appearance configuration
 * 
 * might add an option where this will provide a default config
 * upon encountering an error or no files exist
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Component
public class WebsiteAppearanceConfigLoader {
    private WebsiteAppearanceConfig config;
    private final ApplicationFolder folder;
    
    @Autowired
    public WebsiteAppearanceConfigLoader(ApplicationFolder folder){
        config = null;
        this.folder = folder;
    }
    
    public void setConfig(WebsiteAppearanceConfig config){
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
    
    public WebsiteAppearanceConfig getConfig(){
        if(config == null){
            if(fileExists()){
                readFromFile();
            } else {
                config = new WebsiteAppearanceConfig();
            }
        }
        return config;
    }
    
    private boolean fileExists(){
        return false;
    }
    
    private void readFromFile(){
        // todo set config after reading from file
    }
}
