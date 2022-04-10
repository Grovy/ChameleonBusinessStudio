package com.compilercharisma.chameleonbusinessstudio.webconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
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
            @RequestParam("banner") MultipartFile banner
    ){
        // need to do this way so the files get saved
        // serv.setConfig won't do that
        serv.setOrganizationName(organizationName);
        serv.setSplashPageContent(splash);
        serv.setLogo(logo);
        serv.setBanner(banner);
        return "yay";
    }
}
