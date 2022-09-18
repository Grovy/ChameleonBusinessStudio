package com.compilercharisma.chameleonbusinessstudio;

import com.compilercharisma.chameleonbusinessstudio.config.VendiaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.HttpHeaders;

@SpringBootApplication
public class ChameleonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChameleonApplication.class, args);
    }

    @Bean
    public HttpGraphQlClient httpGraphQlClient(VendiaConfig vendiaConfig) {
        return HttpGraphQlClient.builder()
                .url(vendiaConfig.getBaseUrl())
                .header(HttpHeaders.AUTHORIZATION, vendiaConfig.getApiKey())
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

}
