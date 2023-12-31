package icekubit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(
        name = "Locations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"Name", "Latitude", "Longitude"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "ID")
    private User user;
}
