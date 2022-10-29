package com.compilercharisma.chameleonbusinessstudio.config;

import java.io.IOException;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.compilercharisma.chameleonbusinessstudio.repository.FileSystemWebsiteConfigurationRepository;
import com.compilercharisma.chameleonbusinessstudio.repository.JsonScheduleRepository;
import com.compilercharisma.chameleonbusinessstudio.webconfig.ApplicationFolder;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible for setting up the application when Spring Boot
 * starts running.
 */
@Component
@Slf4j
public class StartupConfig implements ApplicationListener<ApplicationReadyEvent> {

    private final ApplicationFolder folder;
    private final JsonScheduleRepository schedules;
    private final FileSystemWebsiteConfigurationRepository websiteConfig;

    public StartupConfig(
            ApplicationFolder folder,
            JsonScheduleRepository schedules,
            FileSystemWebsiteConfigurationRepository websiteConfig
    ){
        this.folder = folder;
        this.schedules = schedules;
        this.websiteConfig = websiteConfig;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.debug("Running StartupConfig...");

        try {
            folder.createAbsentFolders();
        } catch (IOException ex){
            log.error("A fatal error occured while creating application folders", ex);
            throw new RuntimeException(ex);
        }

        try {
            schedules.createAbsentSchedules();
        } catch(Exception ex){
            log.error("A fatal error occured while storing schedules", ex);
            throw new RuntimeException(ex);
        }

        try {
            websiteConfig.load(); // creates the file if it does not exist
        } catch(Exception ex){
            log.error("A fatal error occured while loading the website configuration file", ex);
            throw new RuntimeException(ex);
        }

        log.debug("... done with StartupConfig");
    }
}
