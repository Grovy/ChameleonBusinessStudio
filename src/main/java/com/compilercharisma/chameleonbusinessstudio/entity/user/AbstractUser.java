package com.compilercharisma.chameleonbusinessstudio.entity.user;

import com.compilercharisma.chameleonbusinessstudio.entity.UserEntity;

/**
 * proxy design pattern
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public abstract class AbstractUser {
    private final UserEntity asEntity;
    
    public AbstractUser(UserEntity asEntity){
        this.asEntity = asEntity;
    }
    
    public UserEntity getAsEntity(){
        return asEntity;
    }
    
    public String getEmail(){
        return asEntity.getEmail();
    }
    
    public String getDisplayName(){
        return asEntity.getDisplayName();
    }
    
    public abstract boolean isAllowedToConfigure();
    public abstract boolean isAllowedToBookForParticipants();
    
    @Override
    public String toString(){
        return String.format("%s (%s)", getDisplayName(), getEmail());
    }
}
