package com.compilercharisma.chameleonbusinessstudio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse;
import com.compilercharisma.chameleonbusinessstudio.service.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path="/api/v1/users")
@Slf4j
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Gets all users from Vendia
     *
     * @return {@link UserResponse}
     */
    @GetMapping() // implies "all"
    public Mono<ResponseEntity<UserResponse>> getAllUsers() {
        log.info("Retrieving all users from Vendia...");
        return userService.getAllUsers()
                .map(r -> new ResponseEntity<>(r, HttpStatus.ACCEPTED))
                .doOnNext(r -> log.info("Finished retrieving all users from Vendia!"))
                .doOnError(e -> log.error("Could not retrieve users from Vendia"));
    }

    /**
     * Creates a user in Vendia
     *
     * @param user the user to be created
     * @return The {@link User} that was created
     */
    @PostMapping()
    public Mono<ResponseEntity<User>> createUser(@RequestBody User user){
        log.info("Creating user in Vendia with parameters [{}]", user);
        return userService.createUser(user)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .doOnNext(u -> log.info("User created in Vendia share!"))
                .onErrorMap(e -> new Exception("Error creating user in Vendia"));
    }

    /**
     * Updates a user in Vendia
     * @param user the user being edited
     * @return The {@link User} that was created
     */
    @PutMapping()
    public Mono<ResponseEntity<User>> updateUser(@RequestBody User user){
        log.info("Updating user in Vendia with parameters [{}]", user);
        return userService.updateUser(user)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .doOnNext(u -> log.info("User updated in Vendia share!"))
                .doOnError(u -> log.error("Something unexpected happened!"));
    }

    /**
     * Deletes user in Vendia
     * @param user the user being removed
     * @return Id of the deleted user
     */
    @DeleteMapping()
    public Mono<ResponseEntity<String>> deleteUser(@RequestBody User user) {
        log.info("Deleting user in Vendia with email [{}]", user.getEmail());
        return userService.deleteUser(user)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .doOnNext(u -> log.info("User deleted in Vendia share!"))
                .doOnError(u -> log.error("Something unexpected happened!"));
    }

}
