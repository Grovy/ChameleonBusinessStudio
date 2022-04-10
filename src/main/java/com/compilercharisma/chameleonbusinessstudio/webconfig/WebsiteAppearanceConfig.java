package com.compilercharisma.chameleonbusinessstudio.webconfig;

import java.io.Serializable;

/**
 * data object representing how the organization has configured their instance
 * of the application.
 * 
 * don't bother storing this in the database, as only one configuration will
 * need to exist at a time
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class WebsiteAppearanceConfig implements Serializable {
    private String splashPageName;
    private String logoName;
    private String bannerName;
    private String organizationName;
    
    public void setSplashPageName(String splashPageName){
        this.splashPageName = splashPageName;
    }
    
    public String getSplashPageName(){
        return splashPageName;
    }
    
    public void setLogoName(String logoName){
        this.logoName = logoName;
    }
    
    public String getLogoName(){
        return logoName;
    }
    
    public void setBannerName(String bannerName){
        this.bannerName = bannerName;
    }
    
    public String getBannerName(){
        return bannerName;
    }
    
    public void setOrganizationName(String organizationName){
        this.organizationName = organizationName;
    }
    
    public String getOrganizationName(){
        return organizationName;
    }
}
