package com.compilercharisma.chameleonbusinessstudio.controller;

import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse;
import com.compilercharisma.chameleonbusinessstudio.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path="/api/users")
@Slf4j
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Get all users from Vendia
     *
     * @return {@link UserResponse}
     */
    @GetMapping("/getAllUsers")
    public Mono<ResponseEntity<UserResponse>> fetchAllUsersFromVendia() {
        log.info("Retrieving all users in Vendia...");
        return userService.getAllUsers()
                .map(r -> new ResponseEntity<>(r, HttpStatus.ACCEPTED))
                .doOnNext(r -> log.info("Finished retrieving all users from Vendia!"));
    }

    /**
     * Create a user in Vendia
     *
     * @param user the user to be created
     * @return The {@link User} that was created
     */
    @PostMapping("/createUser")
    public Mono<ResponseEntity<String>> createVendiaUser(@RequestBody User user){
        return userService.createUser(user)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .onErrorMap(e -> new Exception("Error creating user in Vendia"))
                .doOnNext(u -> log.info("User created in Vendia share!"))
                .doOnError(u -> log.error("Something unexpected happened!"));
    }

    /**
     * Updates a user in Vendia
     * @param user the user being edited
     * @return The {@link User} that was created
     */
    @PutMapping("/updateUser")
    public Mono<ResponseEntity<User>> updateVendiaUser(@RequestBody User user){
        return userService.updateUser(user)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .doOnNext(u -> log.info("User updated in Vendia share!"))
                .doOnError(u -> log.error("Something unexpected happened!"));
    }

    /**
     * Deletes a given user from Vendia
     * @param user the user being removed
     * @return Id of the deleted user
     */
    @DeleteMapping("/deleteUser")
    public Mono<ResponseEntity<String>> deleteVendiaUser(@RequestBody User user) {
        return userService.deleteUser(user)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .doOnNext(u -> log.info("User updated in Vendia share!"))
                .doOnError(u -> log.error("Something unexpected happened!"));
    }

}
