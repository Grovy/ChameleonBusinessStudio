package com.compilercharisma.chameleonbusinessstudio.client

import com.compilercharisma.chameleonbusinessstudio.dto.User
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse
import com.compilercharisma.chameleonbusinessstudio.enumeration.UserRole
import org.springframework.graphql.client.GraphQlClient
import org.springframework.graphql.client.HttpGraphQlClient
import reactor.core.publisher.Mono
import spock.lang.Specification

class VendiaClientSpec extends Specification {

    def httpGraphQlClient = Mock(HttpGraphQlClient)
    def sut = new VendiaClient(httpGraphQlClient)

    def "Sending a query to Vendia is successful"() {
        given: "a User object"
        def user = new User(displayName: "Daniel Villavicencio", email: "daniel@chameleon.com", role: UserRole.ADMIN, appointments: [])
        def query = """mutation {
                    add_User(input :{ email: $user.email, displayName: $user.displayName, role: $user.role})
                         { result { email firstName lastName role } } }"""

        when: "the request is sent to Vendia"
        def response = sut.createUser(user)

        then: "the response is correct"
        response
        response.map(u -> u.displayName == user.displayName)
        response.map(u -> u.email == user.email)
        response.map(u -> u.role == user.role)
        response.map(u -> u.appointments == user.appointments)

        and: "the expected interactions occur"
        1 * httpGraphQlClient.document(query) >> GraphQlClient.RetrieveSpec
        1 * GraphQlClient.RetrieveSpec.toEntity(UserResponse.class) >> Mono<User>
        0 * _
    }
}
