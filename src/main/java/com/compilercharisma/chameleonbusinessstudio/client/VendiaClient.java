package com.compilercharisma.chameleonbusinessstudio.client;

import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class VendiaClient {

    private final HttpGraphQlClient httpGraphQlClient;

    public VendiaClient(HttpGraphQlClient httpGraphQlClient) {
        this.httpGraphQlClient = httpGraphQlClient;
    }

    /**
     * Returns all users found in Vendia
     * @return Mono wrapping {@link UserResponse}
     */
    public Mono<UserResponse> getAllUsers() {
        String getAllUsersQuery = """
                query {
                  list_UserItems {
                    _UserItems {
                      _id
                      email
                      age
                      firstName
                      gender
                      lastName
                    }
                  }
                }
                """;
        return getEntity(getAllUsersQuery, UserResponse.class);
    }

    /**
     * Sends a request to Vendia to create a User
     *
     * @param user user details
     * @return Mono wrapping {@link User}
     */
    public Mono<User> createUser(User user){
        String query = """
                mutation {
                    add_User(input :{ 
                    email: "%s", 
                    firstName: "%s",
                    lastName: "%s",
                    role: %s}) {
                          result {
                            email
                            firstName
                            lastName
                            role
                      }
                    }
                  }"""
                .formatted(user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole());
        return sendEntity(query, "add_User", User.class);
    }

    /**
     * Checks if a user is already registered with their email in Vendia
     *
     * @param email email to check
     * @return Mono wrapping {@link Boolean}
     */
    public Mono<Boolean> isUserRegistered(String email) {
        var query = """
                query {
                  list_UserItems(filter: {email: {eq: "%s"}}) {
                    _UserItems {
                      _id
                      email
                      firstName
                      lastName
                      role
                    }
                  }
                }
                """.formatted(email);
        return getEntity(query, UserResponse.class)
                .map(r -> !r.getUsers().isEmpty());
    }

    private <T> Mono<T> getEntity(String graphQlQuery, final Class<T> responseClass) {
        return httpGraphQlClient
                .document(graphQlQuery)
                .retrieve("list_UserItems")
                .toEntity(responseClass);
    }

    public <T> Mono<T> sendEntity(final String graphQlQuery, final String queryName, final Class<T> responseClass) {
        return httpGraphQlClient
                .document(graphQlQuery)
                .retrieve(queryName)
                .toEntity(responseClass);
    }

}
