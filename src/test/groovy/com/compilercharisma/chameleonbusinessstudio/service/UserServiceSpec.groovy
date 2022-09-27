package com.compilercharisma.chameleonbusinessstudio.service

import com.compilercharisma.chameleonbusinessstudio.dto.User
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse
import com.compilercharisma.chameleonbusinessstudio.repository.UserRepository
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Unroll

class UserServiceSpec extends Specification {

    def userRepository = Mock(UserRepository)
    def sut = new UserService(userRepository)

    @Unroll
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
}
