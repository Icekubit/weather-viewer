package icekubit.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import icekubit.dao.LocationDao;
import icekubit.dto.LocationDto;
import icekubit.dto.WeatherDto;
import icekubit.entity.Location;
import icekubit.entity.User;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WeatherService {

    private final static String API_KEY
            = URLEncoder.encode(System.getenv("OPEN_WEATHER_API_KEY"), StandardCharsets.UTF_8);

    private final LocationDao locationDao;

    public WeatherService(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public List<LocationDto> searchLocationsByName(String name) throws IOException, InterruptedException {
        URI uri = URI.create(String.format("https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=%d&appid=%s"
                , name.replaceAll(" ", "_")
                , 5
                , API_KEY));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper.readValue(response.body(), new TypeReference<List<LocationDto>>(){});
    }

    public WeatherDto getWeatherByCoordinates(double latitude, double longitude) throws IOException, InterruptedException {
        URI uri = URI.create(
                String.format("https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=%s"
        , latitude
        , longitude
        , API_KEY
        , "metric"));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper.readValue(response.body(), WeatherDto.class);
    }

    public List<WeatherDto> getUserLocations(User user) throws IOException, InterruptedException {
        List<Location> locations = locationDao.getLocationsForThisUser(user);
        List<WeatherDto> userLocations = new ArrayList<>();
        for (Location location: locations) {
            WeatherDto weatherDto = getWeatherByCoordinates(location.getLatitude(), location.getLongitude());
            weatherDto.setLocationId(location.getId());
            userLocations.add(weatherDto);
        }
        return userLocations;
    }

    public void save(Location location) {
        locationDao.save(location);
    }

    public void deleteLocation(User user, int locationId) {
        locationDao.deleteLocation(user, locationId);
    }
}
