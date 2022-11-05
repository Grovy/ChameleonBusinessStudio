package com.compilercharisma.chameleonbusinessstudio.service;

import org.springframework.stereotype.Service;

import com.compilercharisma.chameleonbusinessstudio.dto.NotificationPreferences;

import reactor.core.publisher.Mono;

@Service
public class NotificationService {
    
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
