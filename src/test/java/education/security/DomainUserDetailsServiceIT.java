package education.security;
import education.EduOnlineApp;
import education.domain.User;
import education.repository.UserRepository;
import education.security.PasswordEncoder;

import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import io.reactivex.Flowable;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(application = EduOnlineApp.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DomainUserDetailsServiceIT {

    private static final String USER_ONE_LOGIN = "test-user-one";
    private static final String USER_ONE_EMAIL = "test-user-one@localhost";
    private static final String USER_TWO_LOGIN = "test-user-two";
    private static final String USER_TWO_EMAIL = "test-user-two@localhost";
    private static final String USER_THREE_LOGIN = "test-user-three";
    private static final String USER_THREE_EMAIL = "test-user-three@localhost";

    @Inject UserRepository userRepository;
    @Inject DatabaseAuthenticationProvider authenticationProvider;

    private User userOne;
    private User userTwo;
    private User userThree;

    @BeforeAll
    public void init() {
        userOne = new User();
        userOne.setLogin(USER_ONE_LOGIN);
        userOne.setPassword(RandomStringUtils.random(60));
        userOne.setActivated(true);
        userOne.setEmail(USER_ONE_EMAIL);
        userOne.setFirstName("userOne");
        userOne.setLastName("doe");
        userOne.setLangKey("en");
        userRepository.save(userOne);

        userTwo = new User();
        userTwo.setLogin(USER_TWO_LOGIN);
        userTwo.setPassword(RandomStringUtils.random(60));
        userTwo.setActivated(true);
        userTwo.setEmail(USER_TWO_EMAIL);
        userTwo.setFirstName("userTwo");
        userTwo.setLastName("doe");
        userTwo.setLangKey("en");
        userRepository.save(userTwo);

        userThree = new User();
        userThree.setLogin(USER_THREE_LOGIN);
        userThree.setPassword(RandomStringUtils.random(60));
        userThree.setActivated(false);
        userThree.setEmail(USER_THREE_EMAIL);
        userThree.setFirstName("userThree");
        userThree.setLastName("doe");
        userThree.setLangKey("en");
        userRepository.save(userThree);
    }

    @Test
    @Transactional
    public void assertThatUserCanBeFoundByLogin() {
        AuthenticationResponse response = Flowable.fromPublisher(authenticationProvider.authenticate(null, new AuthenticationRequest() {
            @Override
            public Object getIdentity() {
                return userOne.getLogin();
            }

            @Override
            public Object getSecret() {
                return userOne.getPassword();
            }
        })).blockingFirst();
        assertThat(response).isNotNull();
        assertThat(((UserDetails) response).getUsername()).isEqualTo(USER_ONE_LOGIN);
    }

    @Test
    @Transactional
    public void assertThatUserCanBeFoundByLoginIgnoreCase() {
        AuthenticationResponse response = Flowable.fromPublisher(authenticationProvider.authenticate(null, new AuthenticationRequest() {
            @Override
            public Object getIdentity() {
                return userOne.getLogin().toUpperCase(Locale.ENGLISH);
            }

            @Override
            public Object getSecret() {
                return userOne.getPassword();
            }
        })).blockingFirst();
        assertThat(response).isNotNull();
        assertThat(((UserDetails) response).getUsername()).isEqualTo(USER_ONE_LOGIN);
    }

    @Test
    @Transactional
    public void assertThatUserCanBeFoundByEmail() {
        AuthenticationResponse response = Flowable.fromPublisher(authenticationProvider.authenticate(null, new AuthenticationRequest() {
            @Override
            public Object getIdentity() {
                return userTwo.getEmail();
            }

            @Override
            public Object getSecret() {
                return userTwo.getPassword();
            }
        })).blockingFirst();
        assertThat(response).isNotNull();
        assertThat(((UserDetails) response).getUsername()).isEqualTo(USER_TWO_LOGIN);
    }

    @Test
    @Transactional
    public void assertThatUserCanNotBeFoundByEmailIgnoreCase() {
        AuthenticationResponse response = Flowable.fromPublisher(authenticationProvider.authenticate(null, new AuthenticationRequest() {
            @Override
            public Object getIdentity() {
                return userTwo.getEmail().toUpperCase(Locale.ENGLISH);
            }

            @Override
            public Object getSecret() {
                return userTwo.getPassword();
            }
        })).blockingFirst();
        assertThat(response).isNotNull();
        assertThat(response.isAuthenticated()).isFalse();
    }

    @Test
    @Transactional
    public void assertThatEmailIsPrioritizedOverLogin() {
        AuthenticationResponse response = Flowable.fromPublisher(authenticationProvider.authenticate(null, new AuthenticationRequest() {
            @Override
            public Object getIdentity() {
                return userOne.getEmail();
            }

            @Override
            public Object getSecret() {
                return userOne.getPassword();
            }
        })).blockingFirst();
        assertThat(response).isNotNull();
        assertThat(((UserDetails) response).getUsername()).isEqualTo(USER_ONE_LOGIN);
    }

    @Test
    @Transactional
    public void assertThatUserNotActivatedExceptionIsThrownForNotActivatedUsers() {
        AuthenticationResponse response = Flowable.fromPublisher(authenticationProvider.authenticate(null, new AuthenticationRequest() {
            @Override
            public Object getIdentity() {
                return userThree.getLogin();
            }

            @Override
            public Object getSecret() {
                return userThree.getPassword();
            }
        })).blockingFirst();
        assertThat(response).isNotNull();
        assertThat(response.isAuthenticated()).isFalse();
    }


    @MockBean(BcryptPasswordEncoder.class)
    PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String rawPassword) {
                return rawPassword;
            }

            @Override
            public boolean matches(String rawPassword, String encodedPassword) {
                return rawPassword.equals(encodedPassword);
            }
        };
    }
}
