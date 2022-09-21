package com.compilercharisma.chameleonbusinessstudio.controller;

import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse;
import com.compilercharisma.chameleonbusinessstudio.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
    public Mono<UserResponse> fetchAllUsersFromVendia() {
       log.info("Fetching all users from Vendia Share...");
       var response = userService.getAllUsers();
       log.info("Finished fetching all users from Vendia!");
       return response;
    }

    /**
     * Create a user in Vendia
     *
     * @param user the user to be created
     * @return {@link User}
     */
    @PostMapping("/createUser")
    public Mono<User> createVendiaUser(@RequestBody User user){
        log.info("Creating a user in Vendia share...");
        var response = userService.createUser(user);
        log.info("Finished creating a user in Vendia!");
        return response;
    }

    @PostMapping("/updateUser")
    public Mono<User> updateVendiaUser(@RequestBody User user){
        log.info("Updating user");
        var response = userService.updateUser(user);
        log.info("Finished updating user");
        return response;
    }

    /**
     *
     * @param email
     * @return
     */
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
