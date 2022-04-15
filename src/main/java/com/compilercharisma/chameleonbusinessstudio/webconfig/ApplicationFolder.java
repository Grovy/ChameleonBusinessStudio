package com.compilercharisma.chameleonbusinessstudio.webconfig;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static final Path ROOT = Paths.get(System.getProperty("user.dir", "./"), "ChameleonBusinessStudio");
    private static final String SPLASH_DIR = "splashes";
    private static final String LOGO_DIR = "logos";
    
    public ApplicationFolder(){
        try {
            createAbsentFolders();
        } catch (IOException ex) {
            Logger.getLogger(ApplicationFolder.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    private void createAbsentFolders() throws IOException{
        String[] dirs = {SPLASH_DIR, LOGO_DIR};
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

    // Had to replace the readString() method with lines() method to be compatible with Java 8 - Daniel
    public String readSplash(String fileName){
        Stream<String> content;
        try {
            content = Files.lines(Paths.get(getSubdir(SPLASH_DIR).toString(), fileName));
        } catch (IOException ex) {
            Logger.getLogger(ApplicationFolder.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        return content.toString();
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
        Path p = Paths.get(ROOT.toString(), "config.properties");
        try {
            config.store(new FileOutputStream(p.toFile()), "no comment");
        } catch (IOException ex) {
            Logger.getLogger(ApplicationFolder.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    
    public Properties readConfig(){
        Properties conf = new Properties();
        try {
            conf.load(Files.newInputStream(Paths.get(ROOT.toString(), "config.properties")));
        } catch (IOException ex) {
            Logger.getLogger(ApplicationFolder.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        return conf;
    }
    
    public boolean fileExists(String fileName){
        return Paths.get(ROOT.toString(), fileName).toFile().exists();
    }
}
