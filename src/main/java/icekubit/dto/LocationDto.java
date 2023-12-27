package icekubit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    private String name;
    private String country;
    private BigDecimal lat;
    private BigDecimal lon;

}
