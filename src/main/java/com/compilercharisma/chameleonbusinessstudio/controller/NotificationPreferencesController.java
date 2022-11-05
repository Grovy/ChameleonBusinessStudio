package com.compilercharisma.chameleonbusinessstudio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compilercharisma.chameleonbusinessstudio.dto.NotificationPreferences;
import com.compilercharisma.chameleonbusinessstudio.service.AuthenticationService;
import com.compilercharisma.chameleonbusinessstudio.service.NotificationService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path="/api/v1/notification-preferences")
public class NotificationPreferencesController {
    
    private final AuthenticationService authenticationService;
    private final NotificationService notifications;

    public NotificationPreferencesController(
            AuthenticationService authenticationService, 
            NotificationService notifications
    ){
        this.authenticationService = authenticationService;
        this.notifications = notifications;
    }

    @GetMapping("/for-user/{email}")
    public Mono<ResponseEntity<NotificationPreferences>> getNotificationPreferencesForUser(
            @PathVariable String email
    ){
        return notifications.getNotificationPreferencesForUser(email)
            .map(np -> ResponseEntity.ok(np));
    }

    @GetMapping("/mine")
    public Mono<ResponseEntity<NotificationPreferences>> getMyNotificationPreferences(
            Authentication token
    ){
        var email = authenticationService.getEmailFrom(token);
        return notifications.getNotificationPreferencesForUser(email)
            .map(np -> ResponseEntity.ok(np));
    }
}
