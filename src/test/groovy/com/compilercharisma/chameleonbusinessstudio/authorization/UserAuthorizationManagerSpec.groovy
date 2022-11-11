package com.compilercharisma.chameleonbusinessstudio.authorization

import com.compilercharisma.chameleonbusinessstudio.dto.User
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse
import com.compilercharisma.chameleonbusinessstudio.enumeration.UserRole
import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException
import com.compilercharisma.chameleonbusinessstudio.repository.UserRepository
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import reactor.core.publisher.Mono
import spock.lang.Specification

class UserAuthorizationManagerSpec extends Specification {

    def userRepository = Mock(UserRepository)
    def sut = new UserAuthorizationManager(userRepository)

    def "check method returns true if given user has appropriate role"() {
        given: "An authentication object which contains the email"
        def basicOAuth2User = new DefaultOAuth2User([], Map.of("email", "chameleon@gmail.com"), "email")
        def token = new OAuth2AuthenticationToken(basicOAuth2User, [], "123456")
        def authenticationMono = Mono.just(token)

        def vendiaResponse = new UserResponse(users: List.of(
                new User(_id: "0183ab8f-e53c-57c2-d6da-8ed0014bd494", displayName: "Isabella Turner",
                        email: "iTurner@chameleon.com", role: UserRole.ORGANIZER, appointments: [])))

        when: "check is called"
        def response = sut.check(authenticationMono, null).block()

        then: "the response is correct"
        response
        response.granted

        and: "the expected interaction occurs"
        1 * userRepository.findUserByEmail("chameleon@gmail.com") >> Mono.just(vendiaResponse)
        0 * _
    }

    def "check method returns false if given user doesn't have appropriate role"() {
        given: "An authentication object which contains the email"
        def basicOAuth2User = new DefaultOAuth2User([], Map.of("email", "chameleon@gmail.com"), "email")
        def token = new OAuth2AuthenticationToken(basicOAuth2User, [], "123456")
        def authenticationMono = Mono.just(token)

        def vendiaResponse = new UserResponse(users: List.of(
                new User(_id: "0183ab8f-e53c-57c2-d6da-8ed0014bd494", displayName: "Isabella Turner",
                        email: "iTurner@chameleon.com", role: UserRole.PARTICIPANT, appointments: [])))

        when: "check is called"
        def response = sut.check(authenticationMono, null).block()

        then: "the response is correct"
        response
        !response.granted

        and: "the expected interaction occurs"
        1 * userRepository.findUserByEmail("chameleon@gmail.com") >> Mono.just(vendiaResponse)
        0 * _
    }

    def "check method throws exception if user is not registered"() {
        given: "An authentication object which contains the email"
        def basicOAuth2User = new DefaultOAuth2User([], Map.of("email", "chameleon@gmail.com"), "email")
        def token = new OAuth2AuthenticationToken(basicOAuth2User, [], "123456")
        def authenticationMono = Mono.just(token)

        def vendiaResponse = new UserResponse(users: [])

        when: "check is called"
        sut.check(authenticationMono, null).block()

        then: "the response is correct"
        def ex = thrown(UserNotRegisteredException)
        ex.message == "User with email [chameleon@gmail.com] not registered"

        and: "the expected interaction occurs"
        1 * userRepository.findUserByEmail("chameleon@gmail.com") >> Mono.just(vendiaResponse)
        0 * _
    }
}