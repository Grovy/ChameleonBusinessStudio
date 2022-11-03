package com.compilercharisma.chameleonbusinessstudio.service;

import com.compilercharisma.chameleonbusinessstudio.dto.DeletionResponse;
import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.dto.UserAppointments;
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse;
import com.compilercharisma.chameleonbusinessstudio.exception.ExternalServiceException;
import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException;
import com.compilercharisma.chameleonbusinessstudio.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method gets all the users in Vendia
     *
     * @return {@link UserResponse}
     */
    public Mono<UserResponse> getAllUsers() {
        return userRepository.findAllUsers();
    }

    /**
     * Returns a Boolean of whether the user is already registered
     *
     * @param email email of the user
     * @return {@link Boolean}
     */
    public Mono<Boolean> isRegistered(String email) {
        return userRepository.findAllUsers()
                .map(UserResponse::getUsers)
                .map(users -> users.stream()
                        .anyMatch(u -> u.getEmail().equalsIgnoreCase(email)));
    }

    /**
     * Get a single user from Vendia based on their email, null if it doesn't exist
     *
     * @param email User's email
     * @return {@link User}
     */
    public Mono<User> getUser(String email) {
        return getAllUsers()
                .map(UserResponse::getUsers)
                .filter(CollectionUtils::isNotEmpty)
                .switchIfEmpty(Mono.error(new UserNotRegisteredException(
                        "User with email [%s] is not registered".formatted(email))))
                .mapNotNull(list -> list.stream()
                        .filter(u -> u.getEmail().equalsIgnoreCase(email))
                        .findFirst().orElse(null));
    }

    /**
     * Creates a user in Vendia Share
     *
     * @param user the user that will be created in Vendia
     * @return {@link User}
     */
    public Mono<User> createUser(User user) {
        return userRepository.isUserRegistered(user.getEmail())
                .filter(Boolean.FALSE::equals)
                .switchIfEmpty(Mono.error(new ExternalServiceException(
                        "User with email [%s] already exists".formatted(user.getEmail()), HttpStatus.CONFLICT)))
                .flatMap(u -> userRepository.createUser(user));
    }

    /**
     * Edits a user's info in Vendia
     *
     * @param user the user whose info will be edited in Vendia
     * @return {@link User}
     */
    public Mono<User> updateUser(User user) {
        return userRepository.findUserIdByEmail(user.getEmail())
                .mapNotNull(list -> list.getUsers().stream().findFirst().orElse(null))
                .flatMap(u -> userRepository.updateUser(user, u.get_id()));
    }

    /**
     * Deletes a user from Vendia
     *
     * @param email The email of user to be deleted.
     * @return {@link UserResponse}
     */
    public Mono<DeletionResponse> deleteUser(String email) {
        return userRepository.findUserIdByEmail(email)
                .mapNotNull(list -> {
                    if (CollectionUtils.isEmpty(list.getUsers())) {
                        log.error("User with email [{}] was not found in Vendia", email);
                        throw new UserNotRegisteredException("User with email [%s] was not found".formatted(email));
                    }
                    return list.getUsers().stream().findFirst().orElse(null);
                })
                .flatMap(u -> userRepository.deleteUser(u.get_id()));
    }

    /**
     *
     * @param _id The _id to look up.
     * @return {@link UserAppointments}
     */
    public Mono<UserAppointments> getUserAppointments(String _id)
    {
        return userRepository.getUserAppointments(_id);
    }

}
