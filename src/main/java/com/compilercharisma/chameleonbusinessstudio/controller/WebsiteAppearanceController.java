package com.compilercharisma.chameleonbusinessstudio.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.compilercharisma.chameleonbusinessstudio.service.WebsiteAppearanceService;

/**
 * Handles configuration requests that require authentication. This includes
 * requests to change the appearance of the website, such as its splash page or
 * landing page details.
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
    public ResponseEntity<Void> handlePost(
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
        return ResponseEntity.ok().build();
    }
    
    /**
     * Handles post request to /api/v1/config/landing-page
     * 
     * @param root a URI builder containing the current request root, such as
     *  http://localhost:8080
     * @param file an HTML file 
     * @return a 201 Created At response if successful
     */
    @PostMapping("landing-page")
    public ResponseEntity<Void> postLandingPage(
            UriComponentsBuilder root,
            @RequestParam("file") MultipartFile file
    ){
        if(!MimeTypeUtils.TEXT_HTML_VALUE.equals(file.getContentType())){
            return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .build();
        }
        /*
        Submit using:
            <form action="/api/v1/config/landing-page" enctype="multipart/form-data" method="POST">
            <input type="file" name="file"/>
        */
        serv.setLandingPage(file);
        
        URI at = root // relative to application root
            .pathSegment("custom", "landing-page")
            .build()
            .toUri();
        
        return ResponseEntity.created(at).build();
    }
}
