package com.compilercharisma.chameleonbusinessstudio.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This class handles CRUD operations associated with users.
 * We most likely won't need this class for the final product, but it is nice to
 * have for testing
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@RestController
@RequestMapping(path="/api/v1/users")
public class UserController {
    
    private final UserService serv;
    
    @Autowired
    public UserController(UserService serv){
        this.serv = serv;
    }
    
    // curl -X POST localhost:8080/api/v1/users/admin?email=johndoe@gmail.com
    @PostMapping("/admin")
    public @ResponseBody String createAdmin(@RequestParam(name="email") String email){
        boolean success = true;
        try {
            serv.createAdmin(email);
        } catch(Exception ex){
            ex.printStackTrace();
            success = false;
        }
        
        return (success) 
                ? String.format("created %s as an admin", email)
                : String.format("failed to create %s as an admin", email);
    }
}
