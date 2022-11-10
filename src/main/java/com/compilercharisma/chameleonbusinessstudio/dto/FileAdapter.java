package com.compilercharisma.chameleonbusinessstudio.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileAdapter {
    /**
     * appended to the start of the generated file name
     */
    @Builder.Default
    private String resourceType = "resource-type-not-set";

    private FilePart filePart;
    private MediaType mediaType;
    private LocalDateTime received;

    public String getFileName(){
        var iso = received.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        /*
         * Ideally, this should be converted to the equivalent file extension,
         * but I've tried 
         * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/accept/MediaTypeFileExtensionResolver.html
         * but it isn't pre-populated, and dependency-injecting it doesn't work,
         * so this is close enough, given our time frame.
         */
        var minorType = mediaType.getSubtype();
        var raw = "%s-%s.%s".formatted(resourceType, iso, minorType);
        var name = raw.replaceAll(":", "_"); // need to escape part of ISO
        return name;
    }

    public static FileAdapter from(FilePart part){
        return FileAdapter.builder()
            .filePart(part)
            .mediaType(part.headers().getContentType())
            .received(LocalDateTime.now())
            .build();
    }
}
