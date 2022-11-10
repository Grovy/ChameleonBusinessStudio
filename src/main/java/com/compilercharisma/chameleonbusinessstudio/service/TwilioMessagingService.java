package com.compilercharisma.chameleonbusinessstudio.service;

import org.springframework.stereotype.Service;

import com.compilercharisma.chameleonbusinessstudio.config.TwilioConfiguration;
import com.compilercharisma.chameleonbusinessstudio.exception.ExternalServiceException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.extern.slf4j.Slf4j;

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
     * @param message            the message's body
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
}
