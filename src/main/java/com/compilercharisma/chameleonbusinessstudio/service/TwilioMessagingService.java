package com.compilercharisma.chameleonbusinessstudio.service;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.dto.User;
import org.springframework.stereotype.Service;

import com.compilercharisma.chameleonbusinessstudio.config.TwilioConfiguration;
import com.compilercharisma.chameleonbusinessstudio.exception.ExternalServiceException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class TwilioMessagingService {

    TwilioConfiguration twilioConfiguration;

    public TwilioMessagingService(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    /**
     * Send an SMS to a user with a valid phone number
     *
     * @param userPhoneNumber the receiver's phone number
     * @param message         the message's body
     */
    public void sendSMSToUser(String userPhoneNumber, String message) {
        var chameleonSID = twilioConfiguration.getSid();
        var chameleonAuthToken = twilioConfiguration.getToken();
        var senderPhoneNumber = new PhoneNumber(twilioConfiguration.getPhoneNumber());
        var receiverPhoneNumber = new PhoneNumber(userPhoneNumber);
        Twilio.init(chameleonSID, chameleonAuthToken);
        log.info("Sending message to user with phone number [{}]", userPhoneNumber);
        try {
            Message.creator(receiverPhoneNumber, senderPhoneNumber, message).create();
        } catch (ExternalServiceException ex) {
            log.error("Could not send message to user with phone number [{}]", userPhoneNumber);
            var msg = "Something went wrong when calling Twilio's API, could not send message";
            throw new ExternalServiceException(msg, ex);
        }
    }

    /**
     * Creates a simple text message to send to a user who has been booked for an appointment
     *
     * @param appointment The appointment that was booked for the user
     * @param user        The user that was booked for the appointment
     * @return The message to be sent
     */
    public String createAppointmentMessage(Appointment appointment, User user) {
        var userDisplayName = user.getDisplayName();
        var title = appointment.getTitle();
        var description = appointment.getDescription();
        var date = appointment.getStartTime().toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        var startTime = appointment.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
        var endTime = appointment.getEndTime().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
        var location = appointment.getLocation();
        return String.format("""
                Hello %s! 
                An appointment has been booked for you.
                Title: %s
                Description: %s
                                        
                Date: %s from %s to %s
                Location: %s
                                        
                Thank you for booking with us!""", userDisplayName, title, description, date, startTime, endTime, location);
    }
}
