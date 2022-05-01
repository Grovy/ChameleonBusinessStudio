package com.compilercharisma.chameleonbusinessstudio.webconfig;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

/**
 * use this to set / get properties of the website's appearance
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Service
public class WebsiteAppearanceService {
    
    private final ApplicationFolder folder;
    private final ConfigProxy configProxy;
    
    @Autowired
    public WebsiteAppearanceService(ApplicationFolder folder){
        this.folder = folder;
        configProxy = new ConfigProxy(folder);
    }
    
    
    /**
     * sets the organization name in the config file to the given value
     * 
     * @param organizationName the new organization name 
     */
    public void setOrganizationName(String organizationName){
        configProxy.getConfig().setProperty(
                ConfigProxy.ORG_NAME, organizationName
        );
        configProxy.save();
    }
    
    /**
     * Returns the name of the organization that owns this application,
     * defaulting to "Chameleon Business Studio"
     * 
     * @return the name of the organization that owns this application 
     */
    public String getOrganizationName(){
        return configProxy.getConfig().getProperty(
                ConfigProxy.ORG_NAME, "Chameleon Business Studio"
        );
    }
    
    /**
     * sets the custom body content of the splash page and saves it
     * 
     * @param file the file to set as the splash page content
     */
    public void setSplashPageContent(MultipartFile file){
        folder.saveSplash(file);
        configProxy.getConfig().setProperty(
                ConfigProxy.SPLASH_NAME, file.getOriginalFilename()
        );
        configProxy.save();
    }
    
    /**
     * returns the custom splash page content, if it has been configured 
     * 
     * @return the HTML content of the custom splash page
     */
    public String getSplashPageContent(){
        String content = "";
        if(configProxy.isConfigured()){
            content = folder.readSplash(configProxy.getConfig().getProperty(ConfigProxy.SPLASH_NAME));
            // strip some HTML formatting
            if(content.contains("<html>")){
                content = content.substring(
                        content.indexOf("<html>") + 6, 
                        content.lastIndexOf("</html>")
                );
            }
            if(content.contains("<body>")){
                content = content.substring(
                        content.indexOf("<body>") + 6, 
                        content.lastIndexOf("</body>")
                );
            }
        }
        return content;
    }
    
    /**
     * sets the website logo to the given image file
     * 
     * @param file the new logo 
     */
    public void setLogo(MultipartFile file){
        folder.saveLogo(file);
        configProxy.getConfig().setProperty(
                ConfigProxy.LOGO_NAME, file.getOriginalFilename()
        );
        configProxy.save();
    }
    
    /**
     * a blank image if no logo exists
     * 
     * @return the bytes of the logo image file
     */
    public byte[] getLogo(){

        byte[] bytes = new byte[]{};
        
        if(configProxy.isConfigured()){
            try {
                InputStream inputStream = folder.readLogo(configProxy.getConfig().getProperty(ConfigProxy.LOGO_NAME));
                ByteArrayHelper byteArrayHelper = new ByteArrayHelper(inputStream);
                bytes = byteArrayHelper.toByteArray();
            } catch(Exception ex){
                throw new RuntimeException(ex);
            }
        }
        
        return bytes;
    }
    
    /**
     * @param color the CSS color string to use for the website banner 
     */
    public void setBannerColor(String color){
        configProxy.getConfig().setProperty(ConfigProxy.BANNER_COLOR, color);
        configProxy.save();
    }
    
    /**
     * defaults to white if the config file isn't available
     * 
     * @return the CSS color to use for the website banner
     */
    public String getBannerColor(){
        return configProxy.getConfig().getProperty(ConfigProxy.BANNER_COLOR, "#ffffff");
    }
    
    /**
     * automatically saves to the config file afterwards
     * 
     * @param config the new configuration options to use 
     */
    public void setConfig(Properties config){
        configProxy.setConfig(config); // automatically saves
    }
}
