package icekubit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "Sessions",
        indexes = @Index(columnList = "ExpiresAt")
)
@Data
@NoArgsConstructor
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime expiresAt;
    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "ID")
    private User user;
}
