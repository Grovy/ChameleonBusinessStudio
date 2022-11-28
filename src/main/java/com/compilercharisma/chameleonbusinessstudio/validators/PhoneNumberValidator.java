package com.compilercharisma.chameleonbusinessstudio.validators;

import com.compilercharisma.chameleonbusinessstudio.annotation.ValidPhoneNumber;
import com.compilercharisma.chameleonbusinessstudio.config.TwilioConfiguration;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.lookups.v1.PhoneNumber;
import org.springframework.beans.factory.annotation.Configurable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Configurable
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private final TwilioConfiguration twilioConfiguration;

    public PhoneNumberValidator(TwilioConfiguration twilioConfiguration){
        this.twilioConfiguration = twilioConfiguration;
    }

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        Twilio.init(twilioConfiguration.getSid(), twilioConfiguration.getToken());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        value = value.replaceAll("[\\s()-}]", "");
        var phoneNumber = new com.twilio.type.PhoneNumber(value);

        try {
            PhoneNumber.fetcher(phoneNumber).setCountryCode("US").fetch();
            return true;
        } catch (ApiException e) {
            if(e.getStatusCode() == 404) {
                return false;
            }
            throw e;
        }
    }
}
