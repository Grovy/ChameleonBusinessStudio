package com.compilercharisma.chameleonbusinessstudio.entity.user;

import com.compilercharisma.chameleonbusinessstudio.entity.UserEntity;

/**
 * trying out the BUILDER design pattern, as UserEntities have a lot of
 * easily-confused fields
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class UserBuilder {
    private UserEntity building;
    
    public UserBuilder(){
        building = null;
    }
    
    public UserBuilder buildUser(){
        building = new UserEntity();
        return this;
    }
    
    public UserBuilder withId(int id){
        building.setId(id);
        return this;
    }
    
    public UserBuilder withEmail(String email){
        building.setEmail(email);
        return this;
    }
    
    public UserBuilder withDisplayName(String displayName){
        building.setDisplayName(displayName);
        return this;
    }
    
    public UserBuilder withRole(String role){
        building.setRole(role);
        return this;
    }
    
    public UserEntity build(){
        UserEntity ret = building;
        building = null;
        return ret;
    }
}
