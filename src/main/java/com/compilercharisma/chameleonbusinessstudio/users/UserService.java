package com.compilercharisma.chameleonbusinessstudio.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Service
public class UserService {
    
    private final UserRepository repo;
    
    @Autowired
    public UserService(UserRepository repo){
        this.repo = repo;
    }
    
    public Admin createAdmin(String email){
        UserEntity user = new UserBuilder()
                .buildUser()
                .withEmail(email)
                .withDisplayName(email) // default to their email as name
                .withRole("admin")
                .build();
        
        return new Admin(repo.save(user));
    }
    
    public AbstractUser get(String email){
        UserEntity e = repo.findUserByEmail(email).orElseThrow();
        // don't like hard-coding strings like this. Probably use some enum-ish class instead
        switch(e.getRole()){
            case "admin":
                return new Admin(e);
            case "organizer":
                return new Organizer(e);
            case "participant":
                return new Participant(e);
            case "talent":
                return new Talent(e);
            default:
                throw new UnsupportedOperationException(String.format("no data class for role \"%s\"!", e.getRole()));
        }
    }
}
