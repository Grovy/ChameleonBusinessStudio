package com.compilercharisma.chameleonbusinessstudio.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.compilercharisma.chameleonbusinessstudio.dto.NotificationPreferences;

import reactor.core.publisher.Mono;

@Service
public class NotificationService {
    
    /**
     * Stores the given notification preferences, overriding a user's previously
     * stored notification preferences.
     * 
     * @param preferences the notification preferences to store
     * @return the stored notification preferences
     */
    public Mono<NotificationPreferences> createNotificationPreferences(
            NotificationPreferences preferences
    ) {
        preferences.set_id(UUID.randomUUID().toString()); // replace this with storing in the repo
        // email should be a unique key in wherever we store notification preferences
        return Mono.just(preferences);
    }

    /**
     * Gets the notification preferences for the user with the given email, or
     * the default notification preferences if the email is not registered or 
     * the user has not yet chosen their notification preferences.
     * 
     * @param email the email of the user to get notification preferences for
     * @return a mono containing the user's notification preferences
     */
    public Mono<NotificationPreferences> getNotificationPreferencesForUser(String email) {
        // we can settle on the default notification preferences later
        var theDefault = NotificationPreferences.builder()
            .email(email)
            .wantsEmailNotifications(false)
            .wantsTextNotifications(false)
            .advanceInMinutes(15)
            .build();
        
        // todo load their preferences from repo, if any.

        return Mono.just(theDefault);
    }
}
