package icekubit.service;

import icekubit.dao.LocationDao;
import icekubit.dao.UserDao;
import icekubit.entity.Location;
import icekubit.entity.User;
import icekubit.exception.ForbiddenActionException;
import icekubit.exception.LocationAlreadyExistsException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserWeatherServiceTest {
    private UserDao userDao;
    private LocationDao locationDao;
    private UserWeatherService userWeatherService;
    @Mock
    private WeatherApiService weatherApiService;

    @BeforeAll
    void init() {
        userDao = new UserDao();
        locationDao = new LocationDao();
        userWeatherService = new UserWeatherService(locationDao, weatherApiService);
    }


    @Test
    void throwExceptionIfUserAlreadyHasLocation() {
        User testUser = User.builder().login("test_user").password("test_password").build();
        userDao.save(testUser);
        testUser = userDao.findByLogin("test_user").get();

        Location testLocation1 = Location.builder().name("test_location")
                .latitude(BigDecimal.valueOf(42))
                .longitude(BigDecimal.valueOf(42))
                .user(testUser)
                .build();
        locationDao.save(testLocation1);

        Location testLocation2 = Location.builder().name("test_location")
                .latitude(BigDecimal.valueOf(42))
                .longitude(BigDecimal.valueOf(42))
                .user(testUser)
                .build();


        assertThrows(LocationAlreadyExistsException.class,
                () -> userWeatherService.save(testLocation2));
    }

    @Test
    void throwExceptionIfUserAttemptsToDeleteAnotherUserLocation() {
        User user1 = User.builder().login("dummy_user1").password("dummy_password1").build();
        User user2 = User.builder().login("dummy_user2").password("dummy_password2").build();
        userDao.save(user1);
        userDao.save(user2);
        Location locationOfTheFirstUser = Location.builder().name("location of the first user")
                .latitude(BigDecimal.valueOf(42))
                .longitude(BigDecimal.valueOf(42))
                .user(user1)
                .build();
        locationDao.save(locationOfTheFirstUser);

        assertThrows(ForbiddenActionException.class,
                () -> userWeatherService.deleteLocation(locationOfTheFirstUser.getId(), user2));
    }

}
