package com.compilercharisma.chameleonbusinessstudio.formdata;

import org.springframework.http.codec.multipart.FilePart;

import lombok.Data;

/**
 * Allows WebFlux to bind incoming submissions from multipart/form-data files
 */
@Data
public class UploadedFile {
    private FilePart file; // binds to <input type="file" name="file"/>
}
