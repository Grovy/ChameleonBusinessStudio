package com.compilercharisma.chameleonbusinessstudio.integration

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext
import org.springframework.context.ApplicationContext
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.status
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Client
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOidcLogin

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "spring.main.web-application-type=reactive")
class UserControllerITSpec extends Specification {

    @Autowired
    ReactiveWebApplicationContext context

    @Autowired
    ApplicationContext applicationContext

    @Autowired
    WebTestClient client

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig()
            .port(8077))

    def setup() {
        client = WebTestClient.bindToApplicationContext(context)
                .configureClient()
                .build()

        stubFor(get(urlPathMatching("/oauth/authorize.*"))
                .willReturn(aResponse()
                        .withStatus(302)
                        .withHeader("Location", "http://localhost:8080/login/oauth2/code/my-oauth-client?code=my-acccess-code&state=${state}")
                        .withTransformers("CaptureStateTransformer")
                )
        );
    }

    def "getAllUsers is successful"() {
        given: "a request"
        def request = client.mutateWith(mockOidcLogin()).get().uri("/api/v2/users/getAllUsers")

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo("F9v4MUqdQuWAh3Wqxe11mteqPfPedUqp78VaQNJt8DSt"))
                .withHeader("content-type", equalTo("application/json"))
                .withHeader("accept", equalTo("application/json"))
                .withRequestBody(equalTo("""{
                          "query": "query { list_UserItems { _UserItems { _id email displayName appointments } } }",
                          "variables": null,
                          "operationName": null
                        }"""))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("vendiaResponses/getAllUsersResponse.json")))

        stubFor(get("/oauth2/authorization/wiremock")
                .willReturn(status(200)))

        when: "the request is sent"
        def response = request.exchange()

        then: "an OK status is returned"
        response.expectStatus()
                .isOk()

    }
}
