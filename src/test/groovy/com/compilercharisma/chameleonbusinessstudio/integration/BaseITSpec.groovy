package com.compilercharisma.chameleonbusinessstudio.integration

import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.ClassRule
import org.junit.Rule
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.context.ApplicationContext
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "spring.main.web-application-type=reactive")
@AutoConfigureWireMock(port = 8081)
abstract class BaseITSpec extends Specification {

    @Autowired
    ReactiveWebApplicationContext context

    @Autowired
    ApplicationContext applicationContext

    @Autowired
    WebTestClient client

    def setup() {
        client = WebTestClient.bindToApplicationContext(context)
                .configureClient()
                .build()
    }

}
