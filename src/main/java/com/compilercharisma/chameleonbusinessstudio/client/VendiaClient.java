package com.compilercharisma.chameleonbusinessstudio.client;

import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;

@Component
public class VendiaClient {

    private final HttpGraphQlClient httpGraphQlClient;

    public VendiaClient(HttpGraphQlClient httpGraphQlClient) {
        this.httpGraphQlClient = httpGraphQlClient;
    }

    /**
     * Method used to call a GraphQl query
     *
     * @param graphQlQuery the GraphQl query
     * @param queryName the name of the GraphQl query
     * @return {@link org.springframework.graphql.client.GraphQlClient.RetrieveSpec}
     */
    public GraphQlClient.RetrieveSpec fetchDataFromVendia(String graphQlQuery, String queryName) {
        return httpGraphQlClient.document(graphQlQuery).retrieve(queryName);
    }
}
