package com.compilercharisma.chameleonbusinessstudio.users;

/**
 * a participant is the default user type
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class Participant extends AbstractUser {

    public Participant(UserEntity asEntity) {
        super(asEntity);
    }

    @Override
    public boolean isAllowedToConfigure() {
        return false;
    }

    @Override
    public boolean isAllowedToBookForParticipants() {
        return false;
    }
    
    @Override
    public String toString(){
        return String.format("Participant %s", super.toString());
    }
}
