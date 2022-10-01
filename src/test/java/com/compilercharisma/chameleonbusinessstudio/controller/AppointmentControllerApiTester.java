/*
 * https://howtodoinjava.com/spring-webflux/webfluxtest-with-webtestclient/
 */
package com.compilercharisma.chameleonbusinessstudio.controller;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test; // IMPORTANT! Need to use this dependency for @Test, not the other one!
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.compilercharisma.chameleonbusinessstudio.ChameleonApplication;
import com.compilercharisma.chameleonbusinessstudio.config.VendiaConfig;
import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;
import com.compilercharisma.chameleonbusinessstudio.service.AppointmentService;
import com.compilercharisma.chameleonbusinessstudio.service.AuthenticationService;
import com.compilercharisma.chameleonbusinessstudio.service.UserService;

// fails to load application context. are we just better off manually testing endpoints?

// Use @Import(Foo.class) if Foo is needed for DI, as classpath scanning is not done in tests
@ExtendWith(SpringExtension.class) // JUnit 4 uses @RunWith(SpringRunner.class)
//@WebFluxTest(AppointmentController.class)
@ContextConfiguration(classes = ChameleonApplication.class)
public class AppointmentControllerApiTester {
    @Autowired
    private ApplicationContext ctx;

    //@Autowired
    private WebTestClient sut;

    @MockBean
    private AppointmentService appointmentsMock;

    @MockBean
    private AuthenticationService authenticationMock;

    @MockBean
    private UserService users;

    @MockBean
    private VendiaConfig vendia;

    @BeforeEach
    public void setup(){
        sut = WebTestClient
            .bindToApplicationContext(ctx)
            .apply(springSecurity())
            .configureClient()
			.filter(basicAuthentication("user", "password"))
            .build();
    }

    // for the life of me, I cannot get this to work
    // wasted 2 hours of my life on $^**!! Spring Security
    //@Test
    @WithMockUser // does not work. Do we need to create a user for this?
    public void bookMe_givenAValidAppointment_returnsUpdated() throws Exception{
        //var user = new Participant(new UserEntity());
        var aValidAppointment = new AppointmentEntity();

        //when(authenticationMock.getLoggedInUserReactive()).thenReturn(user);
        
        var t = sut
            .post()
            .uri(String.format("/api/v1/appointments/book-me/%d", aValidAppointment.getId()))
            .exchange();
        t.expectStatus().is2xxSuccessful();
    }
}