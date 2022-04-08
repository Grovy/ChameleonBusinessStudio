package com.compilercharisma.chameleonbusinessstudio.users;

import lombok.AllArgsConstructor;

import javax.persistence.*;

/**
 * This is the persistence entity for users.
 * I'd advise against directly interact with this class,
 * as using the other classes provided is more convenient.
 * 
 * @author Matt Crow
 */
@Entity
public class UserEntity {
    
    @Id
    private int id;
    
    @Column(nullable=false, unique=true)
    private String email;
    
    @Column(nullable=false) // todo does this need to be unique?
    private String displayName;
    
    /*
    Using a string instead of an enum allows for greater flexibility, as the
    organizer using this application can easily add roles
    */
    @Column(nullable=false)
    private String role;
    
    // todo might need some Google auth token, but email may be enough

    public UserEntity(){
        
    }

    public void setId(int id){
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public String getEmail(){
        return email;
    }
    
    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }
    
    public String getDisplayName(){
        return displayName;
    }
    
    public void setRole(String role){
        this.role = role;
    }
    
    public String getRole(){
        return role;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("UserEntity {%n");
        sb.append(String.format("\tid : %d,%n", id));
        sb.append(String.format("\temail : %s,%n", email));
        sb.append(String.format("\tdisplayName : %s,%n", displayName));
        sb.append(String.format("\trole : %s%n", role));
        sb.append("}");
        return sb.toString();
    }
}
