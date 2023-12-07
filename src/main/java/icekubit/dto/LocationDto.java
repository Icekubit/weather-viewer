package icekubit.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LocationDto {
    private String name;
    private String country;
    private BigDecimal lat;
    private BigDecimal lon;

}
