package com.compilercharisma.chameleonbusinessstudio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "services.twilio")
public class TwilioConfiguration {

    /**
     * Base url for Twilio api
     */
    private String baseUrl;

    /**
     * Twilio account Sid
     */
    private String sid;

    /**
     * Twilio account token
     */
    private String token;

    /**
     * Twilio virtual phone number
     */
    private String phoneNumber;

    /**
     * Twilio message route url
     */
    private String messageRoute;

}
