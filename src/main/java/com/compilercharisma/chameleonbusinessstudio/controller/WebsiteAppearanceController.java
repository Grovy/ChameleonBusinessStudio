package com.compilercharisma.chameleonbusinessstudio.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.compilercharisma.chameleonbusinessstudio.dto.FileAdapter;
import com.compilercharisma.chameleonbusinessstudio.formdata.BannerColor;
import com.compilercharisma.chameleonbusinessstudio.formdata.OrganizationName;
import com.compilercharisma.chameleonbusinessstudio.formdata.UploadedFile;
import com.compilercharisma.chameleonbusinessstudio.formdata.WebsiteConfigForm;
import com.compilercharisma.chameleonbusinessstudio.service.WebsiteAppearanceService;

import reactor.core.publisher.Mono;

/**
 * Handles requests regarding customized website elements, such as its splash 
 * page or landing page details.
 * 
 * Note that all users, including those not logged in, can access the GET 
 * endpoints, while only authorized users can access the other HTTP verbs.
 * To customize who can access each enpoint, {@link com.compilercharisma.chameleonbusinessstudio.config.SecurityConfiguration SecurityConfiguration}
 * 
 * Helpful link:
 * https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-ann-modelattrib-method-args 
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
            @ModelAttribute BannerColor color){
        serv.setBannerColor(color.getBannerColor());
        return ResponseEntity.created(makeUri(root, "banner-color")).build();
    }

    /**
     * <img src="/api/v1/config/banner-image"/>
     * @return banner image bytes
     */
    @GetMapping("/banner-image")
    public ResponseEntity<byte[]> getBannerImage(){
        return ResponseEntity.ok(serv.getBannerImage());
    }

    /**
     * Receives a multipart form containing an image file.
     * 
     * @param root points to the app root
     * @param file from a file input in a multipart form
     * @return a response containing either nothing or an error
     */
    @PostMapping("/banner-image")
    public Mono<ResponseEntity<Void>> setBannerImage(
            UriComponentsBuilder root,
            @ModelAttribute UploadedFile file
    ){
        var adapted = FileAdapter.from(file.getFile());
        adapted.setResourceType("banner-image");
        
        if(!isImage(adapted)){
            return Mono.just(ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .build());
        }

        var at = makeUri(root, "banner-image");
        return serv.setBannerImage(adapted)
            .then(Mono.just(ResponseEntity.created(at).build()));
    }

    // currently unused
    @GetMapping("/landing-page")
    public Map<String, Object> getLandingPageContent(){
        HashMap<String, Object> json = new HashMap<>();
        json.put("content", serv.getLandingPageContent());
        return json;
    }

    /**
     * Handles post request to /api/v1/config/landing-page
     * 
     * <form action="/api/v1/config/landing-page" enctype="multipart/form-data" method="POST">
     * <input type="file" name="file"/>
     * 
     * @param root a URI builder containing the current request root, such as
     *  http://localhost:8080
     * @param file an HTML file 
     * @return a 201 Created At response if successful
     */
    @PostMapping("landing-page")
    public Mono<ResponseEntity<Void>> setLandingPage(
            UriComponentsBuilder root,
            @ModelAttribute UploadedFile file
    ){
        var adapted = FileAdapter.from(file.getFile());
        adapted.setResourceType("landing-page");

        if(!isHtml(adapted)){
            return Mono.just(ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .build());
        }
        
        return serv.setLandingPage(adapted)
            .then(Mono.just(ResponseEntity.created(makeUri(root, "landing-page")).build()));        
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
    public Mono<ResponseEntity<Void>> setLogo(
            UriComponentsBuilder root,
            @ModelAttribute UploadedFile file
    ){
        var adapted = FileAdapter.from(file.getFile());
        adapted.setResourceType("logo");
        
        if(!isImage(adapted)){
            return Mono.just(ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .build());
        }

        return serv.setLogo(adapted)
            .then(Mono.just(ResponseEntity.created(makeUri(root, "logo")).build()));
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
            @ModelAttribute OrganizationName name
    ){
        serv.setOrganizationName(name.getOrganizationName());
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
    public Mono<ResponseEntity<Void>> setSplashPage(
            UriComponentsBuilder root,
            @ModelAttribute UploadedFile file
    ){
        var adapted = FileAdapter.from(file.getFile());
        adapted.setResourceType("splash");

        if(!isHtml(adapted)){
            return Mono.just(ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .build());
        }

        return serv.setSplashPageContent(adapted)
            .then(Mono.just(ResponseEntity.created(makeUri(root, "splash")).build()));
    }
    
    @PostMapping
    public Mono<ResponseEntity<Void>> handlePost(
            @ModelAttribute WebsiteConfigForm form
    ){
        var monos = new ArrayList<Mono<Void>>();

        if(form.getOrganizationName() != null){
            serv.setOrganizationName(form.getOrganizationName());
        }
        if(form.getBannerColor() != null){
            serv.setBannerColor(form.getBannerColor());
        }

        var logo = form.getLogo();
        if(logo != null && isImage(FileAdapter.from(logo))){
            monos.add(serv.setLogo(FileAdapter.from(logo)));
        }

        var banner = form.getBannerImage();
        if(banner != null && isImage(FileAdapter.from(banner))){
            monos.add(serv.setBannerImage(FileAdapter.from(banner)));
        }

        var splash = form.getSplashPage();
        if(splash != null && isHtml(FileAdapter.from(splash))){
            monos.add(serv.setSplashPageContent(FileAdapter.from(splash)));
        }

        var landing = form.getLandingPage();
        if(landing != null && isHtml(FileAdapter.from(landing))){
            monos.add(serv.setLandingPage(FileAdapter.from(landing)));
        }
        
        return Mono.when(monos)
            .then(Mono.just(ResponseEntity.ok().build()));
    }

    private boolean isImage(FileAdapter file){
        var img = MediaType.IMAGE_JPEG.getType();
        var fileType = file.getMediaType().getType();
        return img.equals(fileType);
    }

    private boolean isHtml(FileAdapter file){
        var html = MediaType.TEXT_HTML;
        var fileType = file.getMediaType();
        return html.equals(fileType);
    }
    
    private URI makeUri(UriComponentsBuilder root, String resource){
        return root.pathSegment("api", "v1", "config", resource)
            .build()
            .toUri();
    }
}
