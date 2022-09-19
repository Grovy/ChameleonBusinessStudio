package com.compilercharisma.chameleonbusinessstudio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "services.vendia")
public class VendiaConfig {

    /**
     * The base url for GraphQl queries in Vendia
     */
    private String baseUrl;

    /**
     * The API key required for CRUD operations
     */
    private String apiKey;

}
