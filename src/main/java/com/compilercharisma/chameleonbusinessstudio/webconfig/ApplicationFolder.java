package com.compilercharisma.chameleonbusinessstudio.webconfig;

import com.compilercharisma.chameleonbusinessstudio.repository.FileSystemWebsiteConfigurationRepository;
import com.compilercharisma.chameleonbusinessstudio.repository.IWebsiteConfigurationRepository;
import java.io.*;
import java.nio.file.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private static final String LANDING_PAGES_DIR = "landingPages";
    private static final String LOGO_DIR = "logos";
    private static final String SPLASH_DIR = "splashes";
    
    private final IWebsiteConfigurationRepository websiteConfigRepo;
    
    
    public ApplicationFolder(){
        try {
            createAbsentFolders();
        } catch (IOException ex) {
            Logger.getLogger(ApplicationFolder.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExceptionInInitializerError(ex);
        }
        websiteConfigRepo = new FileSystemWebsiteConfigurationRepository(ROOT);
    }
    
    private void createAbsentFolders() throws IOException{
        String[] dirs = {SPLASH_DIR, LANDING_PAGES_DIR, LOGO_DIR};
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
    
    public void saveLandingPage(MultipartFile file){
        save(LANDING_PAGES_DIR, file);
    }
    
    public void saveSplash(MultipartFile file){
        save(SPLASH_DIR, file);
    }

    public String readSplash(String fileName){
        return readFile(Paths.get(getSubdir(SPLASH_DIR).toString(), fileName).toFile());
    }
    
    public String readLandingPage(String fileName){
        return readFile(Paths.get(getSubdir(LANDING_PAGES_DIR).toString(), fileName).toFile());
    }
    
    /**
     * Reads the entire contents of the given file and returns it.
     * 
     * @param f the file to read
     * @return the file's text contents
     */
    private String readFile(File f){
        Stream<String> content;
        try {
            // Had to replace the readString() method with lines() method to be compatible with Java 8 - Daniel
            content = Files.lines(f.toPath());
        } catch (IOException ex) {
            Logger.getLogger(ApplicationFolder.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        String s = content.collect(Collectors.joining("\n")); // stream to string
        return s;
    }
    
    public void saveLogo(MultipartFile file){
        save(LOGO_DIR, file);
    }
    
    public InputStream readLogo(String fileName){
        InputStream in = null;
        try {
            in = Files.newInputStream(Paths.get(getSubdir(LOGO_DIR).toString(), fileName));
        } catch (IOException ex) {
            Logger.getLogger(ApplicationFolder.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        return in;
    }
    
    private void save(String dirName, MultipartFile contents){
        File f = Paths.get(getSubdir(dirName).toString(), contents.getOriginalFilename()).toFile();
        try (OutputStream out = new FileOutputStream(f)){
            IOUtils.copy(contents.getInputStream(), out);
        } catch (IOException ex) {
            Logger.getLogger(ApplicationFolder.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    
    public void saveConfig(Properties config){
        websiteConfigRepo.store(config);
    }
    
    public Properties readConfig(){
        return websiteConfigRepo.load();
    }
    
    public boolean fileExists(String fileName){
        return Paths.get(ROOT.toString(), fileName).toFile().exists();
    }
}
