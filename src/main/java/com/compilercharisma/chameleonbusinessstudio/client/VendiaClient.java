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
                      firstName
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

    /**
     * Edits a users info in Vendia Share
     * @param user the user whose info will be edited in Vendia
     * @return {@link User}
     */
    public Mono<User> updateUser(User user){
        String updateUserMutation = """
                mutation {
                   update_User(
                     id: "%s"
                     input: {firstName: "%s", lastName: "%s", role: %s, email: "%s"}
                   ) {
                     result {
                       firstName
                       email
                       lastName
                       role
                     }
                   }
                 }
                """.formatted(user.get_id(), user.getFirstName(),
                user.getLastName(),
                user.getRole(), user.getEmail());
        return sendEntity(updateUserMutation, "update_User", User.class);
    }

    /**
     * This deletes a user from the Vendia database
     * Needs to go from user to _id ideally.
     *
     * @param user The user to be deleted.
     * @return {@link UserResponse}
     */
    public Mono<UserResponse> deleteUser(User user) {
        String deleteUserMutation = """
                mutation {
                  remove_User(id: "%s") {
                    transaction {
                      _id
                    }
                  }
                }""".formatted(user.get_id());
        return getEntity(deleteUserMutation, UserResponse.class);
    }

    private Mono<UserResponse> getUserId(String email) {
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
        return getEntity(query, UserResponse.class);
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
