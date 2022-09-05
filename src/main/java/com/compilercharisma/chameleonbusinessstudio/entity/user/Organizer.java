package com.compilercharisma.chameleonbusinessstudio.entity.user;

import com.compilercharisma.chameleonbusinessstudio.entity.UserEntity;
import com.compilercharisma.chameleonbusinessstudio.entity.user.AbstractUser;

/**
 * an organizer is one of the people who "owns" a particular implementation of
 * this scheduling application template
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class Organizer extends AbstractUser {

    public Organizer(UserEntity asEntity) {
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
        return String.format("Organizer %s", super.toString());
    }
}
