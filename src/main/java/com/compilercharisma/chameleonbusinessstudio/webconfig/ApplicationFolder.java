package com.compilercharisma.chameleonbusinessstudio.webconfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import wiremock.org.apache.commons.io.IOUtils;

/**
 * This class is responsible for providing access to the ChameleonBusinessStudio
 * folder, and thus the various customized website files
 *
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Component
@Slf4j
public class ApplicationFolder {
    public static final Path ROOT = Paths.get(System.getProperty("user.home", "./"), "ChameleonBusinessStudio");
    private static final String LANDING_PAGES_DIR = "landingPages";
    private static final String BANNER_IMAGE_DIR = "bannerImages";
    private static final String LOGO_DIR = "logos";
    private static final String SPLASH_DIR = "splashes";
    public static final String SCHED_DIR = "schedules"; // rm this once we store schedules in Vendia

    private final Path root;

    @Autowired // marks this as the default CTOR
    public ApplicationFolder(){
        this(ROOT);
    }

    public ApplicationFolder(Path root){
        this.root = root;
    }

    /**
     * Creates the folders required by the application, if they do not already
     * exist.
     * 
     * @throws IOException if any errors occur while creating folders
     */
    public void createAbsentFolders() throws IOException{
        String[] dirs = {
            SPLASH_DIR,
            LANDING_PAGES_DIR,
            BANNER_IMAGE_DIR,
            LOGO_DIR,
            SCHED_DIR
        };
        Path p;
        for(String dir : dirs){
            p = getSubdir(dir);
            if(!p.toFile().exists()){
                log.info("Creating " + p.toString());
                Files.createDirectories(p);
            }
        }
    }

    private Path getSubdir(String dirName){
        return Paths.get(root.toString(), dirName);
    }

    public void saveBannerImage(MultipartFile file){
        save(BANNER_IMAGE_DIR, file);
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

    public File getFile(String subdir, String fileName){
        return Paths.get(getSubdir(subdir).toString(), fileName).toFile();
    }

    /**
     * Reads the entire contents of the given file and returns it.
     *
     * @param f the file to read
     * @return the file's text contents
     */
    private String readFile(File f){
        var content = "";
        try (var stream = Files.lines(f.toPath())){
            content = stream.collect(Collectors.joining("\n"));
        } catch (IOException ex) {
            log.error("Error reading file " + f.getAbsolutePath(), ex);
            throw new RuntimeException(ex);
        }
        return content;
    }

    public void saveLogo(MultipartFile file){
        save(LOGO_DIR, file);
    }

    public byte[] readBannerImage(String fileName){
        return readImage(BANNER_IMAGE_DIR, fileName);    
    }

    public byte[] readLogo(String fileName){
        return readImage(LOGO_DIR, fileName);
    }

    private byte[] readImage(String dir, String fileName){
        byte[] bytes = new byte[]{};
        var path = Paths.get(getSubdir(dir).toString(), fileName);

        try (var inputStream = Files.newInputStream(path)){
            ByteArrayHelper byteArrayHelper = new ByteArrayHelper(inputStream);
            bytes = byteArrayHelper.toByteArray();
        } catch(Exception ex){
            log.error("Error reading logo " + path, ex);
            throw new RuntimeException(ex);
        }

        return bytes;
    }

    private void save(String dirName, MultipartFile contents){
        File f = Paths.get(getSubdir(dirName).toString(), contents.getOriginalFilename()).toFile();
        try (
                InputStream in = contents.getInputStream();
                OutputStream out = new FileOutputStream(f);
        ){
            IOUtils.copy(in, out);
        } catch (IOException ex) {
            log.error("Error saving file " + contents.getOriginalFilename(), ex);
            throw new RuntimeException(ex);
        }
    }
}
