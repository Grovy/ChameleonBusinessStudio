package com.compilercharisma.chameleonbusinessstudio.integration

import com.compilercharisma.chameleonbusinessstudio.dto.User
import com.compilercharisma.chameleonbusinessstudio.enumeration.UserRole
import org.springframework.security.test.context.support.WithMockUser

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor

@WithMockUser
class UserControllerITSpec extends BaseITSpec{

    def "getAllUsers is successful"() {
        given: "a request"
        def request = client.get().uri("/api/v2/users/getAllUsers")

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo("F9v4MUqdQuWAh3Wqxe11mteqPfPedUqp78VaQNJt8DSt"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo("{\"query\":\"query { list_UserItems { _UserItems { _id email displayName appointments } } }\"}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/getAllUsersResponse.json")))

        when: "the request is sent"
        def response = request.exchange()

        then: "an OK status is returned"
        response.expectStatus().isAccepted()
                .expectBody()
                .jsonPath('$.users.size()').isEqualTo(23)
                .jsonPath('$.users[?(@._id == "0183764f-3b7d-2277-29d3-be7c8c43c93b")].email').isEqualTo("daniel@chameleon")
                .jsonPath('$.users[?(@._id == "0183764f-3b7d-2277-29d3-be7c8c43c93b")].displayName').isEqualTo("Daniel Villavicencio")
                .jsonPath('$.users[?(@._id == "0183764f-3b7d-2277-29d3-be7c8c43c93b")].appointments.size()').isEqualTo(0)
    }

    def "createUser is successful"() {
        given: "a valid request"
        def user = new User(displayName: "ChameleonTest", email: "Chameleon@email.com", role: UserRole.PARTICIPANT)
        def request = client.post().uri("/api/v2/users/createUser").bodyValue(user)

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo("F9v4MUqdQuWAh3Wqxe11mteqPfPedUqp78VaQNJt8DSt"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo("{\"query\":\"query { list_UserItems { _UserItems { _id email displayName appointments } } }\"}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/getAllUsersResponse.json")))

        when: "the request is sent"
        def response = request.exchange()

        then: ""
    }

}
