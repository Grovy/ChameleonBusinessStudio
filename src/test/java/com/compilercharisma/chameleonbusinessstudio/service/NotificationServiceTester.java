package com.compilercharisma.chameleonbusinessstudio.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NotificationServiceTester {
    
    @Test
    public void getNotificationPreferencesForUser_givenAnEmailWithPreferences_returnsDefaultPreferences(){
        var sut = new NotificationService();
        var theEmail = "foo.bar@baz.qux";
        
        var actual = sut.getNotificationPreferencesForUser(theEmail).block();

        Assertions.assertNotNull(actual);
    }
}
