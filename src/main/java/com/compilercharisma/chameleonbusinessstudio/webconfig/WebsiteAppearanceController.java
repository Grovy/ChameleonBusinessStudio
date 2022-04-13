package com.compilercharisma.chameleonbusinessstudio.webconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * handles configuration requests that require authentication
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@RestController
@RequestMapping(path="/api/v1/config")
public class WebsiteAppearanceController {
    private final WebsiteAppearanceService serv;
    
    @Autowired
    public WebsiteAppearanceController(WebsiteAppearanceService serv){
        this.serv = serv;
    }
    
    @PostMapping
    public String handlePost(
            @RequestParam("org-name") String organizationName,
            @RequestParam("splash") MultipartFile splash,
            @RequestParam("logo") MultipartFile logo,
            @RequestParam("banner-color") String bannerColor
    ){
        // need to do this way so the files get saved
        // serv.setConfig won't do that
        serv.setOrganizationName(organizationName);
        serv.setSplashPageContent(splash);
        serv.setLogo(logo);
        serv.setBannerColor(bannerColor);
        return "yay";
    }
}
