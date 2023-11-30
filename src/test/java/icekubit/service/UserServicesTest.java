package icekubit.service;


import icekubit.dao.UserDao;
import icekubit.dao.UserSessionDao;
import icekubit.entity.User;
import icekubit.exception.UserAlreadyExistException;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServicesTest {
    private UserDao userDao;
    private UserSessionDao userSessionDao;
    private RegistrationService registrationService;
    private AuthorizationService authorizationService;

    @BeforeAll
    void init() {
        userDao = new UserDao();
        userSessionDao = new UserSessionDao();
        registrationService = new RegistrationService(userDao);
        authorizationService = new AuthorizationService(userSessionDao, userDao);
    }

    @BeforeEach
    void prepare() {
        registrationService.registerUser("username", "password");
    }


    @Test
    void userIsPresentInDatabaseAfterRegistration() {
        Optional<User> maybeUser = userDao.getUserByUsername("username");
        assertThat(maybeUser).isPresent();
    }

    @Test
    void throwExceptionIfUserAlreadyExist() {
        assertThrows(UserAlreadyExistException.class,
                () -> registrationService.registerUser("username", "password"));

    }

    @Test
    void userCantAuthorizeAfterAfterSessionIsExpired() throws InterruptedException {
        String userSessionId = authorizationService.authorizeUser("username", "password").toString();
        assertThat(authorizationService.getUserForThisSession(userSessionId)).isPresent();
        Thread.sleep(2000);
        assertThat(authorizationService.getUserForThisSession(userSessionId)).isEmpty();
    }

    @AfterEach
    void cleanDatabase() {
        userSessionDao.deleteAll();
        userDao.deleteAll();
    }
}
