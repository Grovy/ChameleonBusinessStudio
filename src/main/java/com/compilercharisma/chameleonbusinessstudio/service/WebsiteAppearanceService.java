package com.compilercharisma.chameleonbusinessstudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.compilercharisma.chameleonbusinessstudio.repository.WebsiteConfigurationRepository;
import com.compilercharisma.chameleonbusinessstudio.webconfig.ApplicationFolder;

/**
 * use this to set / get properties of the website's appearance
 *
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Service
public class WebsiteAppearanceService {
    private static final String LANDING_PAGE_CONTENT = "pages.landing.content";
    private static final String SPLASH_PAGE_CONTENT = "pages.splash.content";
    private static final String BANNER_COLOR = "banner.color";
    private static final String BANNER_IMAGE = "banner.image";
    private static final String ORG_NAME = "organization.name";
    private static final String LOGO_NAME = "logo.filename";

    private final ApplicationFolder folder;
    private final WebsiteConfigurationRepository repo;

    @Autowired
    public WebsiteAppearanceService(
            ApplicationFolder folder, 
            WebsiteConfigurationRepository repo
    ){
        this.folder = folder;
        this.repo = repo;
    }


    /**
     * sets the organization name in the config file to the given value
     *
     * @param organizationName the new organization name
     */
    public void setOrganizationName(String organizationName){
        repo.set(ORG_NAME, organizationName);
    }

    /**
     * Returns the name of the organization that owns this application,
     * defaulting to "Chameleon Business Studio"
     *
     * @return the name of the organization that owns this application
     */
    public String getOrganizationName(){
        return repo.get(ORG_NAME, "Chameleon Business Studio");
    }

    /**
     * sets the custom body content of the splash page and saves it
     *
     * @param file the file to set as the splash page content
     */
    public void setSplashPageContent(MultipartFile file){
        folder.saveSplash(file);
        repo.set(SPLASH_PAGE_CONTENT, file.getOriginalFilename());
    }

    /**
     * returns the custom splash page content, if it has been configured
     *
     * @return the HTML content of the custom splash page
     */
    public String getSplashPageContent(){
        String content = "";
        if(repo.isConfigured(SPLASH_PAGE_CONTENT)){
            content = folder.readLandingPage(repo.get(SPLASH_PAGE_CONTENT));
            content = extractHtmlBody(content);
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
        repo.set(LOGO_NAME, file.getOriginalFilename());
    }

    /**
     * a blank image if no logo exists
     *
     * @return the bytes of the logo image file
     */
    public byte[] getLogo(){
        byte[] bytes = new byte[]{};

        if(repo.isConfigured(LOGO_NAME)){
            bytes = folder.readLogo(repo.get(LOGO_NAME));
        }

        return bytes;
    }

    /**
     * @param color the CSS color string to use for the website banner
     */
    public void setBannerColor(String color){
        repo.set(BANNER_COLOR, color);
    }

    /**
     * defaults to white if the config file isn't available
     *
     * @return the CSS color to use for the website banner
     */
    public String getBannerColor(){
        return repo.get(BANNER_COLOR, "#ffffff");
    }

    /**
     * defaults to a blank image if the banner hasn't been configured yet
     * 
     * @return the banner image bytes
     */
    public byte[] getBannerImage(){
        byte[] bytes = new byte[]{};

        if(repo.isConfigured(BANNER_IMAGE)){
            bytes = folder.readBannerImage(repo.get(BANNER_IMAGE));
        }

        return bytes;
    }

    /**
     * Sets & stores the given HTML file as the landing page content.
     *
     * @param file an HTML file, uploaded in a multipart form
     */
    public void setLandingPage(MultipartFile file){
        repo.set(LANDING_PAGE_CONTENT, file.getOriginalFilename());
        folder.saveLandingPage(file);
    }

    /**
     * returns the custom landing content, or the default if it hasn't been
     * configured yet
     *
     * @return the HTML content of the custom landing page
     */
    public String getLandingPageContent(){
        String content = "";
        if(repo.isConfigured(LANDING_PAGE_CONTENT)){
            content = folder.readLandingPage(repo.get(LANDING_PAGE_CONTENT));
            content = extractHtmlBody(content);
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
}
