package com.compilercharisma.chameleonbusinessstudio.controller;

import java.util.HashMap;
import java.util.Map;

import com.compilercharisma.chameleonbusinessstudio.service.WebsiteAppearanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * handles requests for the customized website elements
 * (organization name, logo, banner, splash content)
 *
 * this needs to be separate from the CustomizationController, as that one
 * is supposed to secure all its routes.
 *
 * anything served by this controller is accessible to unauthenticated users,
 * so be sure to exclude sensitive information!
 *
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@RestController
@RequestMapping(path="/custom")
public class CustomController {
    private final WebsiteAppearanceService serv;

    @Autowired
    public CustomController(WebsiteAppearanceService serv){
        this.serv = serv;
    }

    /**
     * @return {
     *  name: string
     * }
     */
    @GetMapping("/organization")
    public Map<String, Object> getOrganizationName(){
        HashMap<String, Object> json = new HashMap<>();
        json.put("name", serv.getOrganizationName());
        return json;
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

    /**
     * <img src="/custom/logo"/>
     * @return logo bytes
     */
    @GetMapping("/logo")
    public byte[] getLogo(){
        return serv.getLogo();
    }

    @GetMapping("/landing-page")
    public Map<String, Object> getLandingPageContent(){
        HashMap<String, Object> json = new HashMap<>();
        json.put("content", serv.getLandingPageContent());
        return json;
    }

    /**
     * https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/color
     * @return {
     *  color: string
     * }
     */
    @GetMapping("/banner")
    public Map<String, Object> getBannerColor(){
        HashMap<String, Object> json = new HashMap<>();
        json.put("color", serv.getBannerColor());
        return json;
    }
}
