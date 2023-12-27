package icekubit.service;


import icekubit.dao.UserDao;
import icekubit.dao.UserSessionDao;
import icekubit.entity.User;
import icekubit.exception.UserAlreadyExistsException;
import icekubit.util.PropertiesUtil;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegistrationAndAuthorizationServicesTest {
    private UserDao userDao;
    private UserSessionDao userSessionDao;
    private RegistrationService registrationService;
    private AuthorizationService authorizationService;
    private final String TEST_LOGIN = "test_login";
    private final String TEST_PASSWORD = "test_password";

    @BeforeAll
    void init() {
        userDao = new UserDao();
        userSessionDao = new UserSessionDao();
        registrationService = new RegistrationService(userDao);
        authorizationService = new AuthorizationService(userSessionDao, userDao);
    }

    @BeforeEach
    void prepare() {
        registrationService.registerUser(TEST_LOGIN, TEST_PASSWORD);
    }


    @Test
    void userIsPresentInDatabaseAfterRegistration() {
        Optional<User> maybeUser = userDao.findByLogin(TEST_LOGIN);
        assertThat(maybeUser).isPresent();
    }

    @Test
    void throwExceptionIfUserAlreadyExist() {
        assertThrows(UserAlreadyExistsException.class,
                () -> registrationService.registerUser(TEST_LOGIN, TEST_PASSWORD));

    }

    @Test
    void userCantAuthorizeAfterSessionIsExpired() throws InterruptedException {
        String userSessionId = authorizationService.assignSessionToUser(TEST_LOGIN, TEST_PASSWORD).toString();
        assertThat(authorizationService.getUserForThisSession(userSessionId)).isPresent();
        Thread.sleep(Long.parseLong(PropertiesUtil.get("session.duration")) * 1000);
        assertThat(authorizationService.getUserForThisSession(userSessionId)).isEmpty();
    }

    @AfterEach
    void cleanDatabase() {
        userSessionDao.deleteAll();
        userDao.deleteAll();
    }
}
