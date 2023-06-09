package education.security;

import education.domain.Authority;
import education.domain.User;
import education.repository.UserRepository;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.validation.validator.constraints.EmailValidator;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Singleton
public class DatabaseAuthenticationProvider implements AuthenticationProvider {

    private final Logger log = LoggerFactory.getLogger(DatabaseAuthenticationProvider.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        String username = authenticationRequest.getIdentity().toString();

        log.debug("Authenticating {}", username);

        if (new EmailValidator().isValid(username, null)) {
            return Flowable.just(userRepository.findOneByEmail(username)
                .filter(user -> passwordEncoder.matches(authenticationRequest.getSecret().toString(), user.getPassword()))
                .map(user -> createMicronautSecurityUser(username, user))
                .orElse(new NotAuthenticatedResponse("Invalid username or password")));
        }

        String lowercaseLogin = username.toLowerCase(Locale.ENGLISH);
        return Flowable.just(userRepository.findOneByLogin(lowercaseLogin)
            .filter(user -> passwordEncoder.matches(authenticationRequest.getSecret().toString(), user.getPassword()))
            .map(user -> createMicronautSecurityUser(lowercaseLogin, user))
            .orElse(new NotAuthenticatedResponse("Invalid username or password")));
    }

    private AuthenticationResponse createMicronautSecurityUser(String lowercaseLogin, User user) {
        if (!user.getActivated()) {
            return new NotAuthenticatedResponse("User " + lowercaseLogin + " was not activated");
        }
        List<String> grantedAuthorities = user.getAuthorities().stream()
            .map(Authority::getName)
            .collect(Collectors.toList());

        return new UserDetails(user.getLogin(), grantedAuthorities);
    }
}
