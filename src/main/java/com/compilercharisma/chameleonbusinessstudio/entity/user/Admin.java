package com.compilercharisma.chameleonbusinessstudio.entity.user;

import com.compilercharisma.chameleonbusinessstudio.entity.UserEntity;
import com.compilercharisma.chameleonbusinessstudio.entity.user.AbstractUser;

/**
 * an admin is basically the same as an organizer, except more technical
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class Admin extends AbstractUser {

    public Admin(UserEntity asEntity) {
        super(asEntity);
    }

    @Override
    public boolean isAllowedToConfigure() {
        return true;
    }

    @Override
    public boolean isAllowedToBookForParticipants() {
        return true;
    }
    
    @Override
    public String toString(){
        return String.format("Admin %s", super.toString());
    }
}
