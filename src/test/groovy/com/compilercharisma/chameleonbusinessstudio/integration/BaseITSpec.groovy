package com.compilercharisma.chameleonbusinessstudio.integration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.context.ApplicationContext
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "spring.main.web-application-type=reactive")
@AutoConfigureWireMock(port = 6767)
abstract class BaseITSpec extends Specification {

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

    def setup() {
        client = WebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .build()
    }

}
