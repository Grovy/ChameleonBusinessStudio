package com.compilercharisma.chameleonbusinessstudio.service;

import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse;
import com.compilercharisma.chameleonbusinessstudio.exception.ExternalServiceException;
import com.compilercharisma.chameleonbusinessstudio.repository.UserRepository;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
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

    // temporary method by Matt
    public Mono<Boolean> isRegistered(String email){
        return getAllUsers()
            .map(ur -> ur.getUsers())
            .map(lu -> lu.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email)));
    }

    // temporary method by Matt
    public Mono<Optional<User>> get(String email){
        return getAllUsers()
            .map(ur -> ur.getUsers())
            .map(lu -> lu.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst());
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
