package com.compilercharisma.chameleonbusinessstudio.webconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class is responsible for providing access to the ChameleonBusinessStudio
 * folder, and thus the various customized website files
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Component
public class ApplicationFolder {
    private static final Path ROOT = Paths.get(System.getProperty("user.home", "./"), "ChameleonBusinessStudio");
    private static final String SPLASH_DIR = "splashes";
    private static final String LOGO_DIR = "logos";
    private static final String BANNER_DIR = "banners";
    
    private final ObjectMapper jsonSerializer;
    
    public ApplicationFolder(){
        jsonSerializer = new ObjectMapper();
        
        try {
            createAbsentFolders();
        } catch (IOException ex) {
            Logger.getLogger(ApplicationFolder.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    private void createAbsentFolders() throws IOException{
        String[] dirs = {SPLASH_DIR, LOGO_DIR, BANNER_DIR};
        Path p;
        for(String dir : dirs){
            p = getSubdir(dir);
            if(!p.toFile().exists()){
                Files.createDirectories(p);
            }
        }
    }
    
    private Path getSubdir(String dirName){
        return Paths.get(ROOT.toString(), dirName);
    }
    
    public void saveSplash(MultipartFile file){
        save(SPLASH_DIR, file);
    }
    
    public void saveLogo(MultipartFile file){
        save(LOGO_DIR, file);
    }
    
    public void saveBanner(MultipartFile file){
        save(BANNER_DIR, file);
    }
    
    private void save(String dirName, MultipartFile contents){
        File f = Paths.get(getSubdir(dirName).toString(), contents.getOriginalFilename()).toFile();
        try (OutputStream out = new FileOutputStream(f)){
            IOUtils.copy(contents.getInputStream(), out);
        } catch (IOException ex) {
            Logger.getLogger(ApplicationFolder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void saveConfig(WebsiteAppearanceConfig config){
        Path p = Paths.get(ROOT.toString(), "config.json");
        try {
            String asJson = jsonSerializer.writerWithDefaultPrettyPrinter().writeValueAsString(config);
            Files.write(p, asJson.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(ApplicationFolder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
