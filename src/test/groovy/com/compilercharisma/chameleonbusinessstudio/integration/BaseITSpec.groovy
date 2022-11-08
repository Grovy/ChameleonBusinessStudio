package com.compilercharisma.chameleonbusinessstudio.integration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.context.ApplicationContext
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Shared
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "spring.main.web-application-type=reactive")
@AutoConfigureWireMock(port = 6767)
abstract class BaseITSpec extends Specification {

    def static VENDIA_API_KEY = "F9v4MUqdQuWAh3Wqxe11mteqPfPedUqp78VaQNJt8DSt"

    @Autowired
    ReactiveWebApplicationContext context

    @Autowired
    ApplicationContext applicationContext

    @Autowired
    WebTestClient client

    @MockBean
    ClientRegistrationRepository clientRegistrationRepository

    @Shared
    ObjectMapper objectMapper = new ObjectMapper()

    @Shared
    def basicOAuth2User = new DefaultOAuth2User(
            [], Map.of("email", "super@chameleon.com"), "email")


    def setup() {
        client = WebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .build()

        def email = basicOAuth2User.getAttribute("email")
        def vendiaQuery = "query { list_UserItems(filter: {email: {eq: \\\"$email\\\"}}) { _UserItems { _id displayName email role appointments } } }"
        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo(VENDIA_API_KEY))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo("{\"query\":\"$vendiaQuery\"}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/getLoggedInUserInfoResponse.json")))
    }

}
