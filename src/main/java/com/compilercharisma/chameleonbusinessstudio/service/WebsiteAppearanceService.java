package com.compilercharisma.chameleonbusinessstudio.service;

import java.io.InputStream;
import java.util.Properties;

import com.compilercharisma.chameleonbusinessstudio.webconfig.ApplicationFolder;
import com.compilercharisma.chameleonbusinessstudio.webconfig.ByteArrayHelper;
import com.compilercharisma.chameleonbusinessstudio.config.ProxyConfiguration;
import com.compilercharisma.chameleonbusinessstudio.repository.IWebsiteConfigurationRepository;
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
    private static final String LANDING_PAGE_CONTENT = "pages.landing.content";
    private final ApplicationFolder folder; // migrate away from this
    private final IWebsiteConfigurationRepository repo;
    private final ProxyConfiguration configProxy;
    
    @Autowired
    public WebsiteAppearanceService(ApplicationFolder folder, IWebsiteConfigurationRepository repo){
        this.folder = folder;
        this.repo = repo;
        configProxy = new ProxyConfiguration(folder);
    }
    
    
    /**
     * sets the organization name in the config file to the given value
     * 
     * @param organizationName the new organization name 
     */
    public void setOrganizationName(String organizationName){
        configProxy.getConfig().setProperty(
                ProxyConfiguration.ORG_NAME, organizationName
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
                ProxyConfiguration.ORG_NAME, "Chameleon Business Studio"
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
                ProxyConfiguration.SPLASH_NAME, file.getOriginalFilename()
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
            content = folder.readSplash(configProxy.getConfig().getProperty(ProxyConfiguration.SPLASH_NAME));
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
                ProxyConfiguration.LOGO_NAME, file.getOriginalFilename()
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
                InputStream inputStream = folder.readLogo(configProxy.getConfig().getProperty(ProxyConfiguration.LOGO_NAME));
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
        configProxy.getConfig().setProperty(ProxyConfiguration.BANNER_COLOR, color);
        configProxy.save();
    }
    
    /**
     * defaults to white if the config file isn't available
     * 
     * @return the CSS color to use for the website banner
     */
    public String getBannerColor(){
        return configProxy.getConfig().getProperty(ProxyConfiguration.BANNER_COLOR, "#ffffff");
    }
    
    /**
     * Sets & stores the given HTML file as the landing page content.
     * 
     * @param file an HTML file, uploaded in a multipart form 
     */
    public void setLandingPage(MultipartFile file){
        System.out.println("Content type: " + file.getContentType());
        repo.setValue(LANDING_PAGE_CONTENT, file.getOriginalFilename());
        folder.saveLandingPage(file);
    }
    
    /**
     * returns the custom landing content, or the default if it hasn't been
     * configured yet
     * 
     * @return the HTML content of the custom splash page
     */
    public String getLandingPageContent(){
        String content = "";
        if(configProxy.isConfigured()){
            content = folder.readLandingPage(configProxy.getConfig().getProperty(ProxyConfiguration.SPLASH_NAME));
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
    
    private String extractHtmlBody(String content){
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
        return content;
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
