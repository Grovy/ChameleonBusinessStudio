package com.compilercharisma.chameleonbusinessstudio.formdata;

import org.springframework.http.codec.multipart.FilePart;

import lombok.Data;

@Data
public class WebsiteConfigForm {
    private String organizationName;
    private String bannerColor;
    private FilePart logo;
    private FilePart bannerImage;
    private FilePart splashPage;
    private FilePart landingPage;
}
