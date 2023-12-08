package icekubit.exception;

public class WeatherApiException extends RuntimeException {
    public WeatherApiException(String errorMessage) {
        super(errorMessage);
    }
}
