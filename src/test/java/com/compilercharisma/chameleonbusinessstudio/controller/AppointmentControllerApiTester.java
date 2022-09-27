/*
 * https://howtodoinjava.com/spring-webflux/webfluxtest-with-webtestclient/
 */
package com.compilercharisma.chameleonbusinessstudio.controller;

import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test; // IMPORTANT! Need to use this dependency for @Test, not the other one!
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import com.compilercharisma.chameleonbusinessstudio.config.VendiaConfig;
import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;
import com.compilercharisma.chameleonbusinessstudio.entity.UserEntity;
import com.compilercharisma.chameleonbusinessstudio.entity.appointment.AppointmentModelAssembler;
import com.compilercharisma.chameleonbusinessstudio.entity.user.Participant;
import com.compilercharisma.chameleonbusinessstudio.service.AppointmentService;
import com.compilercharisma.chameleonbusinessstudio.service.AuthenticationService;

// Use @Import(Foo.class) if Foo is needed for DI, as classpath scanning is not done in tests
@ExtendWith(SpringExtension.class) // JUnit 4 uses @RunWith(SpringRunner.class)
@WebFluxTest(AppointmentController.class)
public class AppointmentControllerApiTester {

    @Autowired
    private WebTestClient sut;

    @MockBean
    private AppointmentService appointmentsMock;

    @MockBean
    private AuthenticationService authenticationMock;

    @MockBean
    private AppointmentModelAssembler assemblerMock;

    @MockBean
    private VendiaConfig vendia;

    // for the life of me, I cannot get this to work
    // wasted 2 hours of my life on $^**!! Spring Security
    @Test
    @WithMockUser // does not work. Do we need to create a user for this?
    public void bookMe_givenAValidAppointment_returnsUpdated() throws Exception{
        /*
        var user = new Participant(new UserEntity());
        var aValidAppointment = new AppointmentEntity();

        when(authenticationMock.getLoggedInUser()).thenReturn(user);
        
        
        var t = sut
            .post()
            .uri("/api/v1/appointments/book-me")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(aValidAppointment))
            .exchange();
            t.expectStatus().is2xxSuccessful();
        */
    }
}
