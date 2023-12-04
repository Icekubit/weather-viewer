package icekubit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class WeatherDto {
    private String name;
    private Sys sys;
    private Coordinates coord;
    private Weather[] weather;
    @JsonProperty("main")
    private Temperatures temperatures;

    @Data
    public static class Coordinates {
        private double lon;
        private double lat;
    }

    @Data
    public static class Weather {
        private String description;
    }

    @Data
    public static class Temperatures {
        private double temp;
        private int pressure;
        private int humidity;
    }

    @Data
    public static class Sys {
        private String country;
    }
}
