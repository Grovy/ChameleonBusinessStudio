package com.compilercharisma.chameleonbusinessstudio.client;

import com.compilercharisma.chameleonbusinessstudio.config.TwilioConfiguration;
import com.compilercharisma.chameleonbusinessstudio.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.StringJoiner;

@Component
@Slf4j
public class TwilioClient {

    private final TwilioConfiguration twilioConfiguration;

    public TwilioClient(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    /**
     * Method used to send a message to a user with a valid phone number
     * @param userPhoneNumber the phone number to be sent the message
     * @return {@link Twilio}
     */
    public Mono<Void> createMessage(String userPhoneNumber, String message) {
        var baseUrl = twilioConfiguration.getBaseUrl();
        var twilioPhoneNumber = twilioConfiguration.getPhoneNumber();
        var messageRoute = twilioConfiguration.getMessageRoute();
        var basicAuthCredentials = new StringJoiner(":");
        var webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        basicAuthCredentials.add(twilioConfiguration.getSid());
        basicAuthCredentials.add(twilioConfiguration.getToken());
        try{
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path(messageRoute)
                            .queryParam("From", twilioPhoneNumber)
                            .queryParam("To", userPhoneNumber)
                            .queryParam("Body", message)
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "basic " + basicAuthCredentials)
                    .retrieve()
                    .bodyToMono(Void.class);
        } catch (ExternalServiceException ex) {
            log.error("Something unexpected happened when calling Twilio's api");
            var msg = "Call to Twilio failed!";
            throw new ExternalServiceException(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
