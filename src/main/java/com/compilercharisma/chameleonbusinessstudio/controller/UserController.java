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
     * This endpoint fetches all users from Vendia Share
     *
     * @return {@link UserResponse}
     */
    @GetMapping("/getAll")
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
     * @return {@link User}
     */
    @PostMapping("/createUser")
    public Mono<ResponseEntity<String>> createVendiaUser(@RequestBody User user){
        return userService.createUser(user)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .doOnNext(u -> log.info("User created in Vendia share!"))
                .doOnError(u -> log.error("Something unexpected happened!"));
    }

    @PostMapping("/admin")
    public @ResponseBody String createAdmin(@RequestParam(name="email") String email){
        boolean success = true;
        try {
            userService.createAdmin(email);
        } catch(Exception ex){
            ex.printStackTrace();
            success = false;
        }
        
        return (success) 
                ? String.format("created %s as an admin", email)
                : String.format("failed to create %s as an admin", email);
    }
}
