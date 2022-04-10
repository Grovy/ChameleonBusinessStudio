package com.compilercharisma.chameleonbusinessstudio.webconfig;

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
    private final WebsiteAppearanceConfigLoader loader;
    
    @Autowired
    public WebsiteAppearanceService(ApplicationFolder folder){
        this.folder = folder;
        loader = new WebsiteAppearanceConfigLoader(folder);
    }
    
    public void setSplashPageContent(MultipartFile file){
        loader.getConfig().setSplashPageName(file.getOriginalFilename());
        loader.save();
        folder.saveSplash(file);
    }
    
    public void setLogo(MultipartFile file){
        loader.getConfig().setLogoName(file.getOriginalFilename());
        loader.save();
        folder.saveLogo(file);
    }
    
    public void setBanner(MultipartFile file){
        loader.getConfig().setBannerName(file.getOriginalFilename());
        loader.save();
        folder.saveBanner(file);
    }
    
    public void setOrganizationName(String organizationName){
        loader.getConfig().setOrganizationName(organizationName);
        loader.save();
    }
    
    public void setConfig(WebsiteAppearanceConfig config){
        loader.setConfig(config); // automatically saves
    }
}
