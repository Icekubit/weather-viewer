package icekubit.service;

import icekubit.dto.LocationDto;
import icekubit.exception.WeatherApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WeatherApiServiceTest {
    private HttpClient httpClient;
    private WeatherApiService weatherApiService;

    @BeforeEach
    void prepare() {
        this.httpClient = Mockito.mock(HttpClient.class);
        this.weatherApiService = new WeatherApiService(httpClient);
    }

    @Test
    void answerOfWeatherApiServiceContainsMoscow() throws IOException, InterruptedException {
        String responseBody = "[\n" +
                "    {\n" +
                "        \"name\": \"Moscow\",\n" +
                "        \"lat\": 55.7504461,\n" +
                "        \"lon\": 37.6174943,\n" +
                "        \"country\": \"RU\",\n" +
                "        \"state\": \"Moscow\"\n" +
                "    }\n" +
                "]";
        HttpResponse<String> httpResponse = new HttpResponse<>() {
            @Override
            public int statusCode() {
                return 0;
            }

            @Override
            public HttpRequest request() {
                return null;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return null;
            }

            @Override
            public String body() {
                return responseBody;
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return null;
            }

            @Override
            public HttpClient.Version version() {
                return null;
            }
        };
        doReturn(httpResponse).when(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        LocationDto moscowDto =
                new LocationDto("Moscow", "RU",
                        BigDecimal.valueOf(55.7504461),
                        BigDecimal.valueOf(37.6174943));
        List<LocationDto> locations = weatherApiService.searchLocationsByName("Moscow");
        assertThat(locations).contains(moscowDto);
    }

    @Test
    void searchLocationByNameThrowsExceptionWhenResponseWithErrorStatus() throws IOException, InterruptedException {
        HttpResponse<String> httpResponse = new HttpResponse<>() {
            @Override
            public int statusCode() {
                return 500;
            }

            @Override
            public HttpRequest request() {
                return null;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return null;
            }

            @Override
            public String body() {
                return null;
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return null;
            }

            @Override
            public HttpClient.Version version() {
                return null;
            }
        };
        doReturn(httpResponse).when(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        Assertions.assertThrows(WeatherApiException.class, () -> weatherApiService.searchLocationsByName("El dorado"));
    }
}
