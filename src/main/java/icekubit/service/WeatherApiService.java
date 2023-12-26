package icekubit.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import icekubit.dto.LocationDto;
import icekubit.dto.WeatherDto;
import icekubit.exception.WeatherApiException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class WeatherApiService {

    private final static String API_KEY
            = URLEncoder.encode(System.getenv("OPEN_WEATHER_API_KEY"), StandardCharsets.UTF_8);
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final HttpClient httpClient;

    public WeatherApiService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<LocationDto> searchLocationsByName(String name) throws IOException, InterruptedException {
        URI uri = URI.create(String.format("https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=%d&appid=%s"
                , URLEncoder.encode(name, StandardCharsets.UTF_8)
                , 5
                , API_KEY));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() / 100 == 4 || response.statusCode() / 100 == 5) {
            throw new WeatherApiException("The weather API returned error status");
        }
        return objectMapper.readValue(response.body(), new TypeReference<List<LocationDto>>(){});

    }

    public WeatherDto getWeatherByCoordinates(BigDecimal latitude, BigDecimal longitude)
            throws IOException, InterruptedException {
        URI uri = URI.create(
                String.format("https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=%s"
        , latitude
        , longitude
        , API_KEY
        , "metric"));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() / 100 == 4 || response.statusCode() / 100 == 5) {
            throw new WeatherApiException("The weather API returned error status");
        }
        return objectMapper.readValue(response.body(), WeatherDto.class);
    }
}
