package com.compilercharisma.chameleonbusinessstudio.service;

import com.compilercharisma.chameleonbusinessstudio.client.VendiaClient;
import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse;
import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException;
import com.compilercharisma.chameleonbusinessstudio.entity.UserEntity;
import com.compilercharisma.chameleonbusinessstudio.entity.user.*;
import com.compilercharisma.chameleonbusinessstudio.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final VendiaClient vendiaClient;

    public UserService(UserRepository userRepository, VendiaClient vendiaClient){
        this.userRepository = userRepository;
        this.vendiaClient = vendiaClient;
    }
    
    public Admin createAdmin(String email){
        UserEntity user = new UserBuilder()
                .buildUser()
                .withEmail(email)
                .withDisplayName(email) // default to their email as name
                .withRole(Role.ADMIN)
                .build();
        return new Admin(userRepository.save(user));
    }

    /**
     * Creates a user in Vendia Share
     *
     * @param user the user that will be created in Vendia
     * @return {@link User}
     */
    public Mono<User> createUser(User user){
        String createUserQuery = """
            mutation {
                add_User(input :{age: %s, email: "%s", firstName: "%s", gender: %s, lastName: "%s", role: %s}) {
                          result {
                            age
                            email
                            firstName
                            gender
                            lastName
                            role
                          }
                        }
                      }""".formatted(user.getAge(), user.getEmail(),
                user.getFirstName(), user.getGender(),
                user.getLastName(), user.getRole());
        return vendiaClient.executeRequest(createUserQuery, "add_User").toEntity(User.class);
    }

    /**
     * This method gets all the users from the Vendia
     *
     * @return {@link UserResponse}
     */
    public Mono<UserResponse> getAllUsers() {
        String getAllUsersQuery = """
                query {
                  list_UserItems {
                    _UserItems {
                      _id
                      email
                      age
                      firstName
                      gender
                      lastName
                    }
                  }
                }
                """;
        return vendiaClient.executeRequest(getAllUsersQuery, "list_UserItems")
                .toEntity(UserResponse.class);
    }
    
    /**
     * Note that this method throws a NoSuchElementException if no user
     * exits with the given email.
     * 
     * @param email the email of the user to get
     * 
     * @return the user with the given email. Their role is given by the
     * actual type returned 
     */
    public AbstractUser get(String email){
        UserEntity e = userRepository.findUserByEmail(email).orElseThrow(()->{
            return new UserNotRegisteredException(String.format("No registered user with email %s", email));
        });
        
        switch(e.getRole()){
            case Role.ADMIN:
                return new Admin(e);
            case Role.ORGANIZER:
                return new Organizer(e);
            case Role.PARTICIPANT:
                return new Participant(e);
            case Role.TALENT:
                return new Talent(e);
            default:
                throw new UnsupportedOperationException(String.format("no data class for role \"%s\"!", e.getRole()));
        }
    }

    public void registerUser(UserEntity userEntity){
        userRepository.save(userEntity);
    }
    
    public boolean isRegistered(String email){
        return userRepository.findUserByEmail(email).isPresent();
    }

}
