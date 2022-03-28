package com.compilercharisma.chameleonbusinessstudio.users.authentication;

/**
 * used to encapsulate the Google account details we want to collect from the
 * GoogleAuthenticator
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class GoogleAccountDetails {
    private final String email;
    
    public GoogleAccountDetails(String email){
        this.email = email;
    }
    
    public String getEmail(){
        return email;
    }
}
