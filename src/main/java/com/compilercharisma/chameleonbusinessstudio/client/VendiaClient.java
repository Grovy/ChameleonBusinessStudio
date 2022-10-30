package com.compilercharisma.chameleonbusinessstudio.client;

import com.compilercharisma.chameleonbusinessstudio.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class VendiaClient {

    private final HttpGraphQlClient httpGraphQlClient;

    public VendiaClient(HttpGraphQlClient httpGraphQlClient) {
        this.httpGraphQlClient = httpGraphQlClient;
    }

    /**
     * Method used to execute a GraphQl query
     *
     * @param graphQlQuery  String form of the GraphQl query
     * @param path          Path from where serialization begins
     * @param responseClass The class that should be returned as a response
     * @return {@link Mono}
     */
    public <T> Mono<T> executeQuery(String graphQlQuery, final String path, final Class<T> responseClass) {
        System.out.printf("Executing query \"%s\"...\n", graphQlQuery); // rm once we enable logging
        log.info("Executing query \"%s\"...\n", graphQlQuery);
        return httpGraphQlClient
                .document(graphQlQuery)
                .execute()
                .map(response -> response.field(path).toEntity(responseClass))
                .onErrorResume(error -> {
                    var msg = "Something unexpected happened when calling Vendia";
                    return Mono.error(new ExternalServiceException(msg, HttpStatus.BAD_REQUEST, error));
                });
    }

}
