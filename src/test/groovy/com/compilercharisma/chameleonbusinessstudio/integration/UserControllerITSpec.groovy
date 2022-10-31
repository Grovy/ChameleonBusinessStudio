package com.compilercharisma.chameleonbusinessstudio.integration

import com.compilercharisma.chameleonbusinessstudio.dto.User
import com.compilercharisma.chameleonbusinessstudio.enumeration.UserRole
import org.springframework.http.HttpStatus
import org.springframework.security.test.context.support.WithMockUser

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static com.github.tomakehurst.wiremock.client.WireMock.verify
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOidcLogin

class UserControllerITSpec extends BaseITSpec {

    @WithMockUser
    def "getAllUsers is successful"() {
        given: "a request"
        def request = client.mutateWith(mockOidcLogin()).get().uri("/api/v1/users")

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo(VENDIA_API_KEY))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo("{\"query\":\"query { list_UserItems { _UserItems { _id email displayName role appointments } } }\"}"))
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

    @WithMockUser
    def "createUser is successful"() {
        given: "a valid request"
        def user = new User(_id: "", displayName: "ChameleonTest", email: "Chameleon@email.com", role: UserRole.PARTICIPANT, appointments: [])
        def body = objectMapper.writeValueAsString(user)
        def request = client.mutateWith(mockOidcLogin()).post().uri("/api/v1/users")
                .bodyValue(body)
                .header("content-type", "application/json")

        def vendiaQuery1 = """{"query":"query { list_UserItems(filter: {email: {eq: \\"$user.email\\"}}) { _UserItems { _id email displayName role } } }"}"""
        def vendiaQuery2 = """{"query":"mutation { add_User(input: {appointments: [], displayName: \\"$user.displayName\\", email: \\"$user.email\\", role: $user.role}) { result { _id appointments displayName email role } } }"}"""

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo(VENDIA_API_KEY))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo(vendiaQuery1))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/emptyUsersResponse.json")))

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo(VENDIA_API_KEY))
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

    @WithMockUser
    def "createUser is unsuccessful if there is another user with the associated email in Vendia"() {
        given: "a valid request"
        def user = new User(_id: "", displayName: "userExistsAlready", email: "userAlreadyExists@gmail.com",
                role: UserRole.PARTICIPANT, appointments: [])
        def body = objectMapper.writeValueAsString(user)
        def request = client.mutateWith(mockOidcLogin()).post().uri("/api/v1/users")
                .bodyValue(body)
                .header("content-type", "application/json")

        def vendiaQuery1 = """{"query":"query { list_UserItems(filter: {email: {eq: \\"$user.email\\"}}) { _UserItems { _id email displayName role } } }"}"""

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo(VENDIA_API_KEY))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo(vendiaQuery1))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/userAlreadyExistsResponse.json")))

        when: "the request is sent"
        def response = request.exchange()

        then: "an OK response is returned"
        response.expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath('$.code').isEqualTo("error.message.alreadyExists")
                .jsonPath('$.message').isEqualTo("User with email [userAlreadyExists@gmail.com] already exists")

        and: "two requests to /graphql/ are sent"
        verify(1, postRequestedFor(urlEqualTo("/graphql/")))
    }

    @WithMockUser
    def "getUser is successful"() {
        given: "a valid request"
        def email = "daniel@chameleon"
        def request = client.get().uri("/api/v1/users/$email")

        def vendiaQuery = "{\"query\":\"query { list_UserItems { _UserItems { _id email displayName role appointments } } }\"}"

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo(VENDIA_API_KEY))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo(vendiaQuery))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/getAllUsersResponse.json")))

        when: "the request is sent"
        def response = request.exchange()

        then: "the response is correct"
        response.expectStatus()
                .isOk()
                .expectBody()
                .jsonPath('$._id').isEqualTo("0183764f-3b7d-2277-29d3-be7c8c43c93b")
                .jsonPath('$.displayName').isEqualTo("Daniel Villavicencio")
                .jsonPath('$.email').isEqualTo("daniel@chameleon")
                .jsonPath('$.appointments').isEmpty()
    }

    @WithMockUser
    def "getUser is unsuccessful if user is not found"() {
        given: "a valid request"
        def email = "userIsNotRegistered@gmail.com"
        def request = client.get().uri("/api/v1/users/$email")

        def vendiaQuery = "{\"query\":\"query { list_UserItems { _UserItems { _id email displayName role appointments } } }\"}"

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo(VENDIA_API_KEY))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo(vendiaQuery))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/emptyUsersResponse.json")))

        when: "the request is sent"
        def response = request.exchange()

        then: "the response is correct"
        response.expectStatus().isNotFound()
                .expectBody()
                .jsonPath('$.message').isEqualTo("User with email [userIsNotRegistered@gmail.com] is not registered")
                .jsonPath('$.code').isEqualTo("error.message.resourceNotFound")

    }

    @WithMockUser
    def "deleteUser is successful"() {
        given: "a request"
        def email = "greengrappler12@gmail.com"
        def userId = "01839503-1f82-1357-c21b-20e73e8d5575"
        def request = client.mutateWith(mockOAuth2Login().oauth2User(basicOAuth2User)).delete().uri("/api/v1/users/$email")
        def vendiaQuery = "query { list_UserItems(filter: {email: {eq: \\\"$email\\\"}}) { _UserItems { _id displayName email role appointments } } }"
        def vendiaQuery2 = "mutation { remove_User(id: \\\"$userId\\\") { transaction { _id } } }"

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo(VENDIA_API_KEY))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo("{\"query\":\"$vendiaQuery\"}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/findIdByEmailResponse.json")))

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo(VENDIA_API_KEY))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo("{\"query\":\"$vendiaQuery2\"}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/userDeletionResponse.json")))

        when: "the request is sent"
        def response = request.exchange()

        then: "an OK status is returned"
        response.expectStatus().isOk()
                .expectBody()
                .jsonPath('$._id').isEqualTo("01839503-1f82-1357-c21b-20e73e8d5575")

        and: "two calls to /graphql/ were made"
        verify(3, postRequestedFor(urlEqualTo("/graphql/")))
    }

    @WithMockUser
    def "deleteUser throws an error if user is not found"() {
        given: "a request for a user that does not exist"
        def email = "thisEmailDoesNotExist@gmail.com"
        def request = client.mutateWith(mockOAuth2Login().oauth2User(basicOAuth2User)).delete().uri("/api/v1/users/$email")

        def vendiaQuery = "query { list_UserItems(filter: {email: {eq: \\\"$email\\\"}}) { _UserItems { _id displayName email role appointments } } }"

        stubFor(post("/graphql/")
                .withHeader("Authorization", equalTo(VENDIA_API_KEY))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("Accept", equalTo("application/json, application/graphql+json"))
                .withRequestBody(equalTo("{\"query\":\"$vendiaQuery\"}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("vendiaResponses/emptyFindIdByEmailResponse.json")))

        when: "the request is sent"
        def response = request.exchange()

        then: "an OK status is returned"
        response.expectStatus().isNotFound()
                .expectBody()
                .jsonPath('$.code').isEqualTo("error.message.resourceNotFound")
                .jsonPath('$.message').isEqualTo("User with email [thisEmailDoesNotExist@gmail.com] was not found")

        and: "two calls to /graphql/ were made"
        verify(2, postRequestedFor(urlEqualTo("/graphql/")))
    }
}
