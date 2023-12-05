package icekubit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Data
public class WeatherDto {
    private String name;
    private int temp;
    private int feelsLikeTemp;
    private int pressure;
    private int humidity;
    private int windSpeed;
    private String windDirection;
    private String weather;
    private String descriptionOfWeather;

    @JsonProperty("main")
    private void unpackMain(Map<String, Object> main) {
        this.temp = (int) Math.round((Double) main.get("temp"));
        this.feelsLikeTemp = (int) Math.round((Double) main.get("feels_like"));
        this.pressure = convertHectoPascalToTorr((int) main.get("pressure"));
        this.humidity = (Integer) main.get("humidity");
    }

    @JsonProperty("wind")
    private void unpackWind(Map<String, Object> wind) {
        this.windSpeed = ((Double) wind.get("speed")).intValue();
        this.windDirection = convertDegToDirection((Integer) wind.get("deg"));
    }

    @JsonProperty("weather")
    private void unpackWeather(Map<String, Object>[] weather) {
        this.weather = (String) weather[0].get("main");
        this.descriptionOfWeather = (String) weather[0].get("description");
    }

    private int convertHectoPascalToTorr(int hectoPascalPressure) {
        return (int) Math.round(hectoPascalPressure * 0.75006);
    }

    private String convertDegToDirection(int deg) {
        switch ((int) Math.round(deg / 45.0)) {
            case 0:
                return "north";
            case 1:
                return "northeast";
            case 2:
                return "east";
            case 3:
                return "southeast";
            case 4:
                return "south";
            case 5:
                return "southwest";
            case 6:
                return "west";
            case 7:
                return "northwest";
        }
        throw new RuntimeException("Failed to convert degrees to wind direction");
    }
}
