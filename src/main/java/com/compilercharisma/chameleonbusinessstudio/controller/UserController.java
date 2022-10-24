package com.compilercharisma.chameleonbusinessstudio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse;
import com.compilercharisma.chameleonbusinessstudio.service.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path="/api/v2/users")
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
    @GetMapping("/getUser")
    public Mono<User> getUser(@RequestParam  String email) {
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
    @GetMapping("/getAllUsers")
    public Mono<ResponseEntity<UserResponse>> fetchAllUsersFromVendia() {
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
    @PostMapping("/createUser")
    public Mono<ResponseEntity<User>> createVendiaUser(@RequestBody User user){
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
    @PutMapping("/updateUser")
    public Mono<ResponseEntity<User>> updateVendiaUser(@RequestBody User user){
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
    @DeleteMapping("/deleteUser")
    public Mono<ResponseEntity<String>> deleteVendiaUser(@RequestBody User user) {
        log.info("Deleting user in Vendia with email [{}]", user.getEmail());
        return userService.deleteUser(user)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .doOnNext(u -> log.info("User deleted in Vendia share!"))
                .doOnError(u -> log.error("Something unexpected happened!"));
    }

}
