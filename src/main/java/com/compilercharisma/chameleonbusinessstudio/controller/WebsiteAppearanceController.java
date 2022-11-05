package com.compilercharisma.chameleonbusinessstudio.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.compilercharisma.chameleonbusinessstudio.service.WebsiteAppearanceService;

/**
 * Handles requests regarding customized website elements, such as its splash 
 * page or landing page details.
 * 
 * Note that all users, including those not logged in, can access the GET 
 * endpoints, while only authorized users can access the other HTTP verbs.
 * To customize who can access each enpoint, {@link com.compilercharisma.chameleonbusinessstudio.config.SecurityConfiguration SecurityConfiguration}
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

    /**
     * https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/color
     * @return {
     *  color: string
     * }
     */
    @GetMapping("/banner-color")
    public Map<String, Object> getBannerColor(){
        HashMap<String, Object> json = new HashMap<>();
        json.put("color", serv.getBannerColor());
        return json;
    }

    /**
     * https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/color
     * @param color the new color for the website banner
     * @return a response containing the location of the new banner color
     */
    @PostMapping("/banner-color")
    public ResponseEntity<Void> setBannerColor(
            UriComponentsBuilder root,
            @RequestParam String color){
        serv.setBannerColor(color);
        return ResponseEntity.created(makeUri(root, "banner")).build();
    }

    /**
     * <img src="/api/v1/config/banner-image"/>
     * @return banner image bytes
     */
    @GetMapping("/banner-image")
    public ResponseEntity<byte[]> getBannerImage(){
        return ResponseEntity.ok(serv.getBannerImage());
    }

    @GetMapping("/landing-page")
    public Map<String, Object> getLandingPageContent(){
        HashMap<String, Object> json = new HashMap<>();
        json.put("content", serv.getLandingPageContent());
        return json;
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
    public ResponseEntity<Void> setLandingPage(
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
        return ResponseEntity.created(makeUri(root, "landing-page")).build();
    }

    /**
     * <img src="/api/v1/config/logo"/>
     * @return logo bytes
     */
    @GetMapping("/logo")
    public ResponseEntity<byte[]> getLogo(){
        return ResponseEntity.ok(serv.getLogo());
    }

    /**
     * Sets the website logo
     * @param root relative to website root
     * @param file the image file to use as a logo
     * @return a response containing the location of the new logo
     */
    @PostMapping("/logo")
    public ResponseEntity<Void> setLogo(
            UriComponentsBuilder root,
            @RequestParam("file") MultipartFile file
    ){
        var type = file.getContentType();
        var split = (type == null) ? new String[]{} : type.split("/");
        if(split.length < 1 || !split[0].equals("image")){
            return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .build();
        }
        // todo check mime type if image
        serv.setLogo(file);
        return ResponseEntity.created(makeUri(root, "logo")).build();
    }
    
    /**
     * @return {
     *  name: String
     * }
     */
    @GetMapping("/organization")
    public Map<String, Object> getOrganizationName(){
        HashMap<String, Object> json = new HashMap<>();
        json.put("name", serv.getOrganizationName());
        return json;
    }

    @PostMapping("/organization")
    public ResponseEntity<Void> setOrganizationName(
            UriComponentsBuilder root,
            @RequestParam String name
    ){
        serv.setOrganizationName(name);
        return ResponseEntity.created(makeUri(root, "organization")).build();
    }

    /**
     * @return {
     *  content: string // HTML content
     * }
     */
    @GetMapping("/splash")
    public Map<String, Object> getSplashPageContent(){
        HashMap<String, Object> json = new HashMap<>();
        json.put("content", serv.getSplashPageContent());
        return json;
    }

    @PostMapping("/splash")
    public ResponseEntity<Void> setSplashPage(
            UriComponentsBuilder root,
            @RequestParam MultipartFile file
    ){
        if(!MimeTypeUtils.TEXT_HTML_VALUE.equals(file.getContentType())){
            return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .build();
        }
        serv.setLandingPage(file);        
        return ResponseEntity.created(makeUri(root, "splash")).build();
    }
    
    @PostMapping
    public ResponseEntity<Void> handlePost(
            @RequestParam("org-name") String organizationName,
            @RequestParam("splash") MultipartFile splash,
            @RequestParam("logo") MultipartFile logo,
            @RequestParam("banner-color") String bannerColor
    ){
        serv.setOrganizationName(organizationName);
        serv.setSplashPageContent(splash);
        serv.setLogo(logo);
        serv.setBannerColor(bannerColor);
        return ResponseEntity.ok().build();
    }

    private URI makeUri(UriComponentsBuilder root, String resource){
        return root.pathSegment("api", "v1", "config", resource)
            .build()
            .toUri();
    }
}
