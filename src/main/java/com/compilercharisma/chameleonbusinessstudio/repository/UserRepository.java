package com.compilercharisma.chameleonbusinessstudio.repository;

import com.compilercharisma.chameleonbusinessstudio.client.VendiaClient;
import com.compilercharisma.chameleonbusinessstudio.dto.DeletionResponse;
import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.dto.UserAppointments;
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.util.List;

@Slf4j
@Repository
public class UserRepository {

    private final VendiaClient vendiaClient;

    public UserRepository(VendiaClient vendiaClient) {
        this.vendiaClient = vendiaClient;
    }

    /**
     * Gets all users in Vendia
     *
     * @return {@link UserResponse}
     */
    public Mono<UserResponse> findAllUsers() {
        var query = "query { list_UserItems { _UserItems { _id email displayName role appointments } } }";
        return vendiaClient.executeQuery(query, "list_UserItems", UserResponse.class);
    }

    /**
     * Creates a user in Vendia
     *
     * @param user the user that will be created
     * @return The {@link User} that was created
     */
    public Mono<User> createUser(User user) {
        var query = StringUtils.isBlank(user.getPhoneNumber()) ?
                "mutation { add_User(input: {appointments: [], displayName: \"%s\", email: \"%s\", role: %s}) { result { _id appointments displayName email role } } }"
                        .formatted(user.getDisplayName(), user.getEmail(), user.getRole()) :
                "mutation { add_User(input: {appointments: [], displayName: \"%s\", email: \"%s\", phoneNumber: \"%s\", role: %s}) { result { _id appointments displayName email phoneNumber role } } }"
                        .formatted(user.getDisplayName(), user.getEmail(), user.getPhoneNumber(), user.getRole());
        return vendiaClient.executeQuery(query, "add_User.result", User.class);
    }

    /**
     * Find the user with the specified email
     *
     * @param email email of the user
     * @return The first occurrence of {@link User}
     */
    public Mono<UserResponse> findUserByEmail(String email) {
        var query = "query { list_UserItems(filter: {email: {eq: \"%s\"}}) { _UserItems { _id displayName email role appointments } } }"
                .formatted(email);
        return vendiaClient.executeQuery(query, "list_UserItems", UserResponse.class);
    }

    /**
     * Checks if a user is already registered with their email in Vendia
     *
     * @param email email to check
     * @return True if the user is registered, False if the user is not registered
     */
    public Mono<Boolean> isUserRegistered(String email) {
        var query = """
                query { list_UserItems(filter: {email: {eq: "%s"}}) { _UserItems { _id email displayName role } } }"""
                .formatted(email);
        return vendiaClient.executeQuery(query, "list_UserItems", UserResponse.class)
                .map(r -> CollectionUtils.isNotEmpty(r.getUsers()));
    }

    /**
     * Updates a user in Vendia
     *
     * @param user The user whose info will be edited in Vendia
     * @param id   Id of the user that will be updated
     * @return {@link User}
     */
    public Mono<User> updateUser(User user, String id) {
        String updateUserMutation = "mutation { update_User( id: \"%s\" input: {displayName: \"%s\", role: %s, email: \"%s\"} ) { result { displayName email role appointments } } }"
                .formatted(id, user.getDisplayName(), user.getRole(), user.getEmail());
        return vendiaClient.executeQuery(updateUserMutation, "update_User.result", User.class);
    }

    /**
     * Delete a user from Vendia
     *
     * @param id The id of the user to be deleted.
     * @return {@link String}
     */
    public Mono<DeletionResponse> deleteUser(String id) {
        String deleteUserMutation = "mutation { remove_User(id: \"%s\") { transaction { _id } } }".formatted(id);
        return vendiaClient.executeQuery(deleteUserMutation, "remove_User.transaction", DeletionResponse.class)
                .doOnError(l -> log.error("Something bad happened when executing mutation for deleting user, check syntax"));
    }

    /**
     * Grab a user's appointments via their id.
     *
     * @param _id The id to look up.
     * @return {@link UserAppointments}
     */
    public Mono<UserAppointments> getUserAppointments(String _id) {
        String getUserAppointmentArray = """
                query { get_User(id: "%s") { appointments } }""".formatted(_id);

        return vendiaClient.executeQuery(getUserAppointmentArray, "get_User", UserAppointments.class);
    }

}

