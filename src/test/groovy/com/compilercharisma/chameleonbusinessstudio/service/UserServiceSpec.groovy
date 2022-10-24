package com.compilercharisma.chameleonbusinessstudio.service

import com.compilercharisma.chameleonbusinessstudio.dto.User
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse
import com.compilercharisma.chameleonbusinessstudio.entity.user.Role
import com.compilercharisma.chameleonbusinessstudio.enumeration.UserRole
import com.compilercharisma.chameleonbusinessstudio.repository.UserRepository
import reactor.core.publisher.Mono
import spock.lang.Specification

class UserServiceSpec extends Specification {

    def userRepository = Mock(UserRepository)
    def sut = new UserService(userRepository)

    def "getting all users is successful"() {
        when: "getAllUsers is called"
        def response = sut.getAllUsers()

        then: "the response is correct"
        response
        response.map(r -> r.getUsers())

        and: "the expected interactions occur"
        1 * userRepository.findAllUsers() >> Mono.just(Void)
        0 * _
    }

    def "isUserRegistered returns false if user isn't registered in Vendia"() {
        given: "a user email"
        def email = "user@chameleon.com"
        def users =
                new UserResponse(users: [
                        new User(_id: "0183ab8f-e53c-57c2-d6da-8ed0014bd494", displayName: "Isabella Turner",
                                email: "iTurner@chameleon.com", role: UserRole.ORGANIZER, appointments: []
                        )
                ])

        when: "isUserRegistered is called"
        def response = sut.isRegistered(email).block()

        then: "the response is correct"
        !response

        and: "the expected interactions occur"
        1 * userRepository.findAllUsers() >> Mono.just(users)
        0 * _
    }

    def "isUserRegistered returns true if user exists in Vendia"() {
        given: "a user email"
        def email = "iTurner@chameleon.com"
        def users =
                new UserResponse(users: [
                        new User(_id: "0183ab8f-e53c-57c2-d6da-8ed0014bd494", displayName: "Isabella Turner",
                                email: "iTurner@chameleon.com", role: UserRole.ORGANIZER, appointments: []
                        )
                ])

        when: "isUserRegistered is called"
        def response = sut.isRegistered(email).block()

        then: "the response is correct"
        response

        and: "the expected interactions occur"
        1 * userRepository.findAllUsers() >> Mono.just(users)
        0 * _
    }

    def "getUser is successful"() {
        given: "an email"
        def email = "danielR@chameleon.com"
        def expected = new User(_id: "0184068e-4e86-5eaf-1f1f-71fc9f829887", displayName: "Daniel Ramos",
                email: "danielR@chameleon.com", role: UserRole.ADMIN, appointments: [])

        when: "getUser is called"
        def response = sut.getUser(email).block()

        then: "the response is correct"
        response._id == expected._id
        response.email == expected.email
        response.displayName == expected.displayName
        response.role == expected.role
        response.appointments == expected.appointments

        and: "the expected interactions occur"
        1 * userRepository.findAllUsers() >> Mono.just(new UserResponse([expected]))
        0 * _
    }
}
