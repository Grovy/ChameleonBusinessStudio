package com.compilercharisma.chameleonbusinessstudio.authorization;

import com.compilercharisma.chameleonbusinessstudio.enumeration.UserRole;
import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException;
import com.compilercharisma.chameleonbusinessstudio.repository.UserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class UserAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private static final Set<UserRole> AUTHORIZED_ROLES = Set.of(UserRole.ADMIN, UserRole.TALENT, UserRole.ORGANIZER);
    private final UserRepository userRepository;

    public UserAuthorizationManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Uses the UserRepository to make sure that the logged-in user has enough roles to access certain endpoints
     *
     * @param authentication the authentication object containing the OAuth2.0 token
     * @param object Not needed, just for overriding purposes
     * @return {@link AuthorizationDecision} of whether the user has enough roles or not for the request
     */
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {
        var email = authentication
                .map(a -> (OAuth2AuthenticationToken) a)
                .mapNotNull(p -> (String) p.getPrincipal().getAttribute("email"));
        return isUserAuthorized(email)
                .map(AuthorizationDecision::new);
    }

    private Mono<Boolean> isUserAuthorized(Mono<String> email) {
        return email.flatMap(e -> {
            var users = userRepository.findUserByEmail(e);
            return users.flatMap(userResponse -> {
                if (CollectionUtils.isEmpty(userResponse.getUsers())) {
                    var msg = "User with email [%s] not registered".formatted(e);
                    return Mono.error(new UserNotRegisteredException(msg));
                }
                return Mono.just(AUTHORIZED_ROLES.contains(userResponse.getUsers().get(0).getRole()));
            });
        });
    }

}
