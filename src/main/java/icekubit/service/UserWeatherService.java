package icekubit.service;

import icekubit.dao.LocationDao;
import icekubit.dto.LocationDto;
import icekubit.dto.WeatherDto;
import icekubit.entity.Location;
import icekubit.entity.User;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UserWeatherService {

    private final LocationDao locationDao;
    private final WeatherApiService weatherApiService;

    public UserWeatherService(LocationDao locationDao, WeatherApiService weatherApiService) {
        this.locationDao = locationDao;
        this.weatherApiService = weatherApiService;
    }

    public List<WeatherDto> getUserLocations(User user) throws IOException, InterruptedException {
        List<Location> locations = locationDao.findLocationsByUserId(user.getId());
        List<WeatherDto> userLocations = new ArrayList<>();
        for (Location location: locations) {
            WeatherDto weatherDto = weatherApiService
                    .getWeatherByCoordinates(location.getLatitude(), location.getLongitude());
            weatherDto.setLocationId(location.getId());
            // name in the weather json may be empty, so we assign it from location variable
            weatherDto.setName(location.getName());
            userLocations.add(weatherDto);
        }
        return userLocations;
    }

    public List<LocationDto> searchLocationsByNameAndExcludeSaved(User user, String name)
            throws IOException, InterruptedException {
        List<LocationDto> foundLocations = weatherApiService.searchLocationsByName(name);
        List<Location> userLocations = locationDao.findLocationsByUserId(user.getId());
        return foundLocations.stream()
                .filter(foundLocation -> !isLocationBelongToTheUser(foundLocation, userLocations))
                .collect(Collectors.toList());
    }

    private boolean isLocationBelongToTheUser(LocationDto foundLocation, List<Location> userLocations) {
        return userLocations.stream()
                .anyMatch(userLocation -> Objects.equals(foundLocation.getLat(), userLocation.getLatitude()) &&
                        Objects.equals(foundLocation.getLon(), userLocation.getLongitude()));
    }

    public void save(Location location) {
        locationDao.save(location);
    }

    public void deleteLocation(int locationId) {
        locationDao.delete(locationId);
    }

    public Optional<Location> findLocationById(int locationId) { return locationDao.findLocationById(locationId);}
}
