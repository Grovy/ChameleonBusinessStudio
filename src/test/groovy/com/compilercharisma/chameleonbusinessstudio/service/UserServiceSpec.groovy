package com.compilercharisma.chameleonbusinessstudio.service

import com.compilercharisma.chameleonbusinessstudio.dto.User
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse
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

    def "CreateUser is successful" () {
        given: "a user to be created"
        def user = new User(_id: "", displayName: "ChameleonTest", email: "chameleon@gmail.com")
        def expected = new User(_id: "123456", displayName: "ChameleonTest", email: "chameleon@gmail.com", role: UserRole.PARTICIPANT, appointments: [])

        when: "createUser is called"
        def response = sut.createUser(user).block()

        then: "the response is correct"
        response._id == expected._id
        response.displayName == expected.displayName
        response.email == expected.email
        response.role == expected.role
        response.appointments == expected.appointments

        and: "the expected interactions occur"
        1 * userRepository.isUserRegistered(user.email) >> Mono.just(Boolean.FALSE)
        1 * userRepository.createUser(user) >> Mono.just(expected)
        0 * _
    }

    def "CreateUser is successful when user includes phoneNumber" () {
        given: "a user to be created"
        def user = new User(_id: "", displayName: "ChameleonTest", email: "chameleon@gmail.com", phoneNumber: "+11234567890")
        def expected = new User(_id: "123456", displayName: "ChameleonTest", email: "chameleon@gmail.com",
                role: UserRole.PARTICIPANT, appointments: [], phoneNumber:  "+11234567890")

        when: "createUser is called"
        def response = sut.createUser(user).block()

        then: "the response is correct"
        response._id == expected._id
        response.displayName == expected.displayName
        response.email == expected.email
        response.role == expected.role
        response.phoneNumber == expected.phoneNumber
        response.appointments == expected.appointments

        and: "the expected interactions occur"
        1 * userRepository.isUserRegistered(user.email) >> Mono.just(Boolean.FALSE)
        1 * userRepository.createUser(user) >> Mono.just(expected)
        0 * _
    }
}
