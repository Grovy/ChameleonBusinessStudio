package com.compilercharisma.chameleonbusinessstudio.users;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    public Optional<UserEntity> findUserByEmail(String email);
}
