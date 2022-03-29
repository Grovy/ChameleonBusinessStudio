package com.compilercharisma.chameleonbusinessstudio.users.authentication;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/** 
 * Helpful links:
 * - https://developers.google.com/identity/gsi/web/guides/overview
 * - https://developers.google.com/identity/gsi/web/guides/get-google-api-clientid
 * - https://developers.google.com/identity/gsi/web/guides/verify-google-id-token
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Service
public class GoogleAuthenticator {
    private HttpTransport transport;
    private JsonFactory jsonFactory;
    private GoogleIdTokenVerifier verifier;
    private final Environment env;
    
    @Autowired
    public GoogleAuthenticator(Environment env){
        this.env = env;        
        verifier = new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singleton(env.getProperty("google.client.id")))
                .build();
    }
    
    public GoogleAccountDetails validate(String credential){
        GoogleAccountDetails details = null;
        try {
            details = tryToValidate(credential);
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return details;
    }
    
    private GoogleAccountDetails tryToValidate(String credential) throws GeneralSecurityException, IOException{
        GoogleIdToken token = verifier.verify(credential);
        GoogleAccountDetails details = null;
        if(token != null){
            Payload payload = token.getPayload();
            String email = payload.getEmail();
            details = new GoogleAccountDetails(email);
        }
        return details;
    }
    
}
