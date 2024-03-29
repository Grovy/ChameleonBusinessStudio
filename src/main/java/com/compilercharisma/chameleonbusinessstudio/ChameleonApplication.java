package com.compilercharisma.chameleonbusinessstudio;

import com.compilercharisma.chameleonbusinessstudio.config.VendiaConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
@EnableScheduling // allows appointment generation to run
public class ChameleonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChameleonApplication.class, args);
    }

    @Bean
    public HttpGraphQlClient httpGraphQlClient(VendiaConfiguration vendiaConfiguration) {
        return HttpGraphQlClient.builder()
                .url(vendiaConfiguration.getBaseUrl())
                .header(HttpHeaders.AUTHORIZATION, vendiaConfiguration.getApiKey())
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

    // serves index.html from "/" route
    // https://stackoverflow.com/a/50324512
    @Bean
    public RouterFunction<ServerResponse> indexRouter(
        @Value("classpath:/static/index.html") Resource indexHtml
    ){
        return route(GET("/"),
            req->ok().contentType(MediaType.TEXT_HTML).bodyValue(indexHtml)
        );
    }
}
