package com.compilercharisma.chameleonbusinessstudio.service;

import com.compilercharisma.chameleonbusinessstudio.client.VendiaClient;
import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse;
import com.compilercharisma.chameleonbusinessstudio.exception.ExternalServiceException;
import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException;
import com.compilercharisma.chameleonbusinessstudio.entity.UserEntity;
import com.compilercharisma.chameleonbusinessstudio.entity.user.*;
import com.compilercharisma.chameleonbusinessstudio.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * This method gets all the users from the Vendia
     *
     * @return {@link UserResponse}
     */
    public Mono<UserResponse> getAllUsers() {
        return userRepository.findAllUsers();
    }

    /**
     * Creates a user in Vendia Share
     *
     * @param user the user that will be created in Vendia
     * @return {@link User}
     */
    public Mono<User> createUser(User user) {
        return userRepository.isUserRegistered(user.getEmail())
                .filter(isRegistered -> isRegistered)
                .flatMap(u -> userRepository.createUser(user));
    }

    /**
     * Edits a users info in Vendia Share
     * @param user the user whose info will be edited in Vendia
     * @return {@link User}
     */
    public Mono<User> updateUser(User user){
        return userRepository.findId(user.getEmail())
                .flatMap(u -> userRepository.updateUser(user, u.get_id()));
    }

    /**
     * This deletes a user from the Vendia database
     * Needs to go from user to _id ideally.
     * @param user The user to be deleted.
     * @return {@link UserResponse}
     */
    public Mono<String> deleteUser(User user) {
        return userRepository.findId(user.getEmail())
                .flatMap(u -> userRepository.deleteUser(u.get_id()));
    }

}
