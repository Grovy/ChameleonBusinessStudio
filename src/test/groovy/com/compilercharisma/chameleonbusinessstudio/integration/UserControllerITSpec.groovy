package com.compilercharisma.chameleonbusinessstudio.integration

import com.compilercharisma.chameleonbusinessstudio.dto.User
import com.compilercharisma.chameleonbusinessstudio.enumeration.UserRole
import org.springframework.security.test.context.support.WithMockUser

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static com.github.tomakehurst.wiremock.client.WireMock.verify

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

        and: "only one call to /graphql/ was made"
        verify(1, postRequestedFor(urlEqualTo("/graphql/")))

    }

    def "createUser is successful"() {
        given: "a valid request"
        def user = new User(_id: "", displayName: "ChameleonTest", email: "Chameleon@email.com", role: UserRole.PARTICIPANT, appointments: [])
        def body = objectMapper.writeValueAsString(user)
        def request = client.post().uri("/api/v2/users/createUser")
                .bodyValue(body)
                .header("content-type", "application/json")

        def vendiaQuery1 = """{"query":"query { list_UserItems(filter: {email: {eq: \\"$user.email\\"}}) { _UserItems { _id email displayName role } } }"}"""
        def vendiaQuery2 = """{"query":"mutation { add_User(input: {appointments: [], displayName: \\"$user.displayName\\", email: \\"$user.email\\", role: $user.role}) { result { _id appointments displayName email role } } }"}"""

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo("F9v4MUqdQuWAh3Wqxe11mteqPfPedUqp78VaQNJt8DSt"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo(vendiaQuery1))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/emptyUsersResponse.json")))

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo("F9v4MUqdQuWAh3Wqxe11mteqPfPedUqp78VaQNJt8DSt"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo(vendiaQuery2))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/createUserResponse.json")))

        when: "the request is sent"
        def response = request.exchange()

        then: "an OK response is returned"
        response.expectStatus().isOk()
                .expectBody()
                .jsonPath('$._id').isEqualTo("0184126d-8309-52e1-2e62-30fc43dd5096")
                .jsonPath('$.email').isEqualTo("ChameleonTest@gmail.com")
                .jsonPath('$.displayName').isEqualTo("ChameleonTest")
                .jsonPath('$.role').isEqualTo("PARTICIPANT")
                .jsonPath('$.appointments.size()').isEqualTo(0)

        and: "two requests to /graphql/ are sent"
        verify(2, postRequestedFor(urlEqualTo("/graphql/")))

    }

    def "deleteUser in Vendia is succesful"() {
        given: "An existing user"
        def user = new User(_id: "0184113a-0870-cd1b-271d-ccfb801204dd", displayName: "Daniel V",
                email: "Daniel@gmail.com", role: UserRole.ORGANIZER, appointments: [])
        def findIdByEmailQuery = """{"query":"query { list_UserItems(filter: {email: {eq: \\\"$user.email\\\"}}) { _UserItems { _id displayName email role appointments } } }"}"""

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo("F9v4MUqdQuWAh3Wqxe11mteqPfPedUqp78VaQNJt8DSt"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo(findIdByEmailQuery))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/findIdByEmailResponse.json")))

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo("F9v4MUqdQuWAh3Wqxe11mteqPfPedUqp78VaQNJt8DSt"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo(findIdByEmailQuery))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/findIdByEmailResponse.json")))
    }

}
