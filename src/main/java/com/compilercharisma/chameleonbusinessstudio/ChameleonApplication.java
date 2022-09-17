package com.compilercharisma.chameleonbusinessstudio;

import org.springframework.beans.factory.annotation.Value;
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
    public HttpGraphQlClient httpGraphQlClient(){
        return HttpGraphQlClient.builder()
                .url("${services.vendia.baseUrl}")
                .header(HttpHeaders.AUTHORIZATION, "${API_KEY}")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

}
