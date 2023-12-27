package icekubit.service;

import icekubit.dto.LocationDto;
import icekubit.exception.WeatherApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WeatherApiServiceTest {
    private HttpClient httpClient;
    private WeatherApiService weatherApiService;

    private final String dummyResponseBody = "[\n" +
            "    {\n" +
            "        \"name\": \"Moscow\",\n" +
            "        \"lat\": 55.7504461,\n" +
            "        \"lon\": 37.6174943,\n" +
            "        \"country\": \"RU\",\n" +
            "        \"state\": \"Moscow\"\n" +
            "    }\n" +
            "]";

    private final LocationDto dummyDto = LocationDto.builder()
            .name("Moscow")
            .country("RU")
            .lat(BigDecimal.valueOf(55.7504461))
            .lon(BigDecimal.valueOf(37.6174943))
            .build();

    @BeforeEach
    void prepare() {
        this.httpClient = Mockito.mock(HttpClient.class);
        this.weatherApiService = new WeatherApiService(httpClient);
    }

    @Test
    void answerOfWeatherApiServiceContainsDummyCity() throws IOException, InterruptedException {
        HttpResponse<String> httpResponse = Mockito.mock(HttpResponse.class);
        when(httpResponse.body()).thenReturn(dummyResponseBody);
        doReturn(httpResponse).when(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        List<LocationDto> locations = weatherApiService.searchLocationsByName("Moscow");
        assertThat(locations).contains(dummyDto);
    }

    @Test
    void searchLocationByNameThrowsExceptionWhenResponseWithErrorStatus() throws IOException, InterruptedException {
        HttpResponse<String> errorHttpResponse = Mockito.mock(HttpResponse.class);
        when(errorHttpResponse.statusCode()).thenReturn(500);
        doReturn(errorHttpResponse).when(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        Assertions.assertThrows(WeatherApiException.class, () -> weatherApiService.searchLocationsByName("El dorado"));
    }
}
