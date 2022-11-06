package com.compilercharisma.chameleonbusinessstudio.dto;

import org.springframework.http.codec.multipart.FilePart;

import lombok.Data;

/**
 * Allows WebFlux to bind incoming submissions from multipart/form-data files
 */
@Data
public class UploadedFile {
    private FilePart file;
}
