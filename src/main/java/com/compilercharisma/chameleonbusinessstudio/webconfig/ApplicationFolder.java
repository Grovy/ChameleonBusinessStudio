package com.compilercharisma.chameleonbusinessstudio.webconfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compilercharisma.chameleonbusinessstudio.dto.FileAdapter;
import com.compilercharisma.chameleonbusinessstudio.formdata.SplashContent;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

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
    private static final String BANNER_IMAGE_DIR = "bannerImages";
    private static final String LOGO_DIR = "logos";
    private static final String SPLASH_DIR = "splashes";
    public static final String SCHED_DIR = "schedules";

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

    public Mono<Void> saveBannerImage(FileAdapter file){
        return save(BANNER_IMAGE_DIR, file);
    }

    public Mono<Void> saveSplash(SplashContent content){
        return Mono.just(content.getContent())
                .map(text -> {
                    var f = getFile(SPLASH_DIR, content.getName());
                    try {
                        Files.writeString(f.toPath(), text);
                        log.info("Saved splash page to {}", f.getAbsolutePath());
                    } catch (IOException e) {
                        return Mono.error(e);
                    }
                    return 0;
                }).then();
    }

    public String readSplash(String fileName){
        return readFile(Paths.get(getSubdir(SPLASH_DIR).toString(), fileName).toFile());
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

    public Mono<Void> saveLogo(FileAdapter file){
        return save(LOGO_DIR, file);
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

    private Mono<Void> save(String dirName, FileAdapter contents){
        File f = Paths.get(getSubdir(dirName).toString(), contents.getFileName())
            .toFile();
        return contents.getFilePart().transferTo(f)
            .doOnSuccess(x -> log.info("Saved to {}", f.getAbsolutePath()))
            .doOnError(err -> log.error("Failed to save " + f.getAbsolutePath(), err));
    }
}
