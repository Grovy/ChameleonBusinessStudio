package com.compilercharisma.chameleonbusinessstudio.users;

import com.compilercharisma.chameleonbusinessstudio.users.authentication.AuthenticationService;
import com.compilercharisma.chameleonbusinessstudio.users.authentication.UserNotRegisteredException;
//import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Fetch;
import java.util.Map;

/**
 *
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Service
public class UserService {
    
    private final UserRepository repo;
//    private final AuthenticationService authoenticationService;

    @Autowired
    public UserService(UserRepository repo/*,
                       AuthenticationService authenticationService*/){
        this.repo = repo;
//        this.authenticationService = authenticationService;
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
    
    /**
     * note that this method throws a NoSuchElementException if no user
     * exits with the given email.
     * 
     * @param email the email of the user to get
     * 
     * @return the user with the given email. Their role is given by the
     * actual type returned 
     */
    public AbstractUser get(String email){
        UserEntity e = repo.findUserByEmail(email).orElseThrow(()->{
            return new UserNotRegisteredException(String.format("No registered user with email %s", email));
        });
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

    /* This function registers a user based on their email and display name
     * to our database, it is directly called on the authenticationService class
     */
//    public void registerUser(){
//        OAuth2User oAuth2User = authenticationService.getLoggedInGoogleUser();
//        UserEntity userEntity = new UserEntity();
//        userEntity.setDisplayName(oAuth2User.getAttribute("name"));
//        userEntity.setEmail(oAuth2User.getAttribute("email"));
//        userEntity.setRole("Admin");
//        repo.save(userEntity);
//    }
    
    public boolean isRegistered(String email){
        return repo.findUserByEmail(email).isPresent();
    }
}
