package com.compilercharisma.chameleonbusinessstudio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a user's notification preferences.
 * These preferences are used to send email and text messages about appointments
 * the user is booked for.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationPreferences {
    
    /**
     * Used by Vendia
     */
    private String _id;

    /**
     * the email of the user these notification preferences are for
     */
    private String email;

    /**
     * If set to true, the user will receive email notifications about their
     * appointments.
     */
    private Boolean wantsEmailNotifications;

    /**
     * If set to true, the user will receive text (SMS) notifications about
     * their appointments.
     */
    private Boolean wantsTextNotifications;

    /**
     * How far in advance the user wants to receive notifications, measured in
     * minutes.
     * For example, a value of 15 indicates the user wants to receive 
     * notifications 15 minutes before an event.
     */
    private Integer advanceInMinutes;
}
