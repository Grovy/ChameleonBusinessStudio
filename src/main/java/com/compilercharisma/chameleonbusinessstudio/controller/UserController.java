package com.compilercharisma.chameleonbusinessstudio.controller;

import com.compilercharisma.chameleonbusinessstudio.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * Get a user from Vendia by their email
     * @param email User's email
     *
     * @return {@link UserResponse}
     */
    @GetMapping("/{email}")
    public Mono<User> getUser(@PathVariable String email) {
        log.info("Retrieving user with email [{}] from Vendia", email);
        return userService.getUser(email)
                .doOnNext(l -> log.info("Finished retrieving user from Vendia with email [{}]", email))
                .doOnError(e -> log.error("Could not retrieve user from Vendia with email [{}]", email));
    }

    /**
     * Gets all users from Vendia
     *
     * @return {@link UserResponse}
     */
    @GetMapping()
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
                .doOnError(e -> log.error("Could not create user in Vendia"));
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
     * @param email the email of user being removed
     * @return Id of the deleted user
     */
    @DeleteMapping("/{email}")
    public Mono<ResponseEntity<DeletionResponse>> deleteUser(@PathVariable String email) {
        log.info("Deleting user in Vendia with email [{}]", email);
        return userService.deleteUser(email)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .doOnNext(u -> log.info("User deleted in Vendia share!"))
                .doOnError(u -> log.error("Something unexpected happened!"));
    }

    /**
     * Get all appointments for a user based on their id
     *
     * @param _id of the user
     * @return the appointments associated with the _id
     * from the user.
     */
    @GetMapping("/getUserAppointments/{id}")
    public Mono<ResponseEntity<UserAppointmentsResponse>> getUserAppointments(@PathVariable(value="id") String _id)
    {
        log.info("Fetching User's appointment(s) for user with id [{}]", _id);
        return userService.getUserAppointments(_id)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .doOnNext(u -> log.info("User's appointment(s) have been received!"));
    }

}
