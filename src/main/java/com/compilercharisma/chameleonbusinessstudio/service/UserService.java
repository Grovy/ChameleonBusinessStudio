package com.compilercharisma.chameleonbusinessstudio.service;

import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse;
import com.compilercharisma.chameleonbusinessstudio.exception.ExternalServiceException;
import com.compilercharisma.chameleonbusinessstudio.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
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
    public Mono<Boolean> isRegistered(String email){
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
    public Mono<User> getUser(String email){
        return getAllUsers()
            .map(UserResponse::getUsers)
            .mapNotNull(lu -> lu.stream()
                    .filter(u -> u.getEmail().equalsIgnoreCase(email))
                        .findFirst().orElseThrow());
    }

    /**
     * Creates a user in Vendia Share
     *
     * @param user the user that will be created in Vendia
     * @return {@link User}
     */
    public Mono<User> createUser(User user) {
        return Mono.just(user)
                .filterWhen(u -> userRepository.isUserRegistered(u.getEmail()).map(b -> !b))
                .switchIfEmpty(Mono.error(new ExternalServiceException("User already exists with given email", HttpStatus.BAD_REQUEST)))
                .flatMap(u -> userRepository.createUser(user));
    }

    /**
     * Edits a user's info in Vendia
     *
     * @param user the user whose info will be edited in Vendia
     * @return {@link User}
     */
    public Mono<User> updateUser(User user){
        return userRepository.findId(user.getEmail())
                .mapNotNull(list -> list.getUsers().stream().findFirst().orElse(null))
                .flatMap(u -> userRepository.updateUser(user, u.get_id()));
    }

    /**
     * Deletes a user from Vendia
     *
     * @param user The user to be deleted.
     * @return {@link UserResponse}
     */
    public Mono<String> deleteUser(User user) {
        return userRepository.findId(user.getEmail())
                .mapNotNull(list -> list.getUsers().stream().findFirst().orElse(null))
                .flatMap(u -> userRepository.deleteUser(u.get_id()));
    }

}
