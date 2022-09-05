package com.compilercharisma.chameleonbusinessstudio.entity.user;

import com.compilercharisma.chameleonbusinessstudio.entity.UserEntity;
import com.compilercharisma.chameleonbusinessstudio.entity.user.AbstractUser;

/**
 * a talent is employed by the organizer
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class Talent extends AbstractUser {

    public Talent(UserEntity asEntity) {
        super(asEntity);
    }

    @Override
    public boolean isAllowedToConfigure() {
        return false;
    }

    @Override
    public boolean isAllowedToBookForParticipants() {
        return true;
    }
    
    @Override
    public String toString(){
        return String.format("Talent %s", super.toString());
    }
}
