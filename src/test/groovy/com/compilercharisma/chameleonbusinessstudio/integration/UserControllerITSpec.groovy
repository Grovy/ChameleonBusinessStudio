package com.compilercharisma.chameleonbusinessstudio.integration

import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.eclipse.jetty.http.HttpHeader
import org.junit.Rule
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext
import org.springframework.context.ApplicationContext
import org.springframework.graphql.test.tester.HttpGraphQlTester
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import java.net.http.HttpHeaders

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "spring.main.web-application-type=reactive")
class UserControllerITSpec extends Specification {

    @Autowired
    ReactiveWebApplicationContext context

    @Autowired
    ApplicationContext applicationContext

    @Autowired
    WebTestClient client

    @Autowired
    WebTestClient graphqlClient

    @Autowired
    HttpGraphQlTester tester

    @Rule
    public WireMockRule wiremock = new WireMockRule(wireMockConfig().port(8081))

    def setup() {
        client = WebTestClient.bindToApplicationContext(context)
                .configureClient()
                .build()

        graphqlClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8081/graphql/")
                .defaultHeader("Authorization", "F9v4MUqdQuWAh3Wqxe11mteqPfPedUqp78VaQNJt8DSt")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build()

        tester = HttpGraphQlTester.create(graphqlClient)
    }

    @WithMockUser
    def "getAllUsers is successful"() {
        given: "a request"
        def request = client.get().uri("/api/v2/users/getAllUsers")

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo("F9v4MUqdQuWAh3Wqxe11mteqPfPedUqp78VaQNJt8DSt"))
                .withHeader("Content-Type", equalTo("application/graphql+json"))
                .withHeader("Accept", equalTo("application/json"))
                .withRequestBody(equalTo("""{ "query": "query { list_UserItems { _UserItems { _id email displayName appointments } } }" }"""))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/getAllUsersResponse.json")))

        when: "the request is sent"
        def response = request.exchange()

        then: "an OK status is returned"
        response.expectStatus()
                .isOk()

    }
}
