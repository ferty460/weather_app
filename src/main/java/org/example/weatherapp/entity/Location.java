package org.example.weatherapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @EqualsAndHashCode.Include
    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @EqualsAndHashCode.Include
    @Column(name = "longitude", precision = 10, scale = 8)
    private BigDecimal longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location other)) return false;

        return Objects.equals(name, other.name)
                && latitude != null
                && longitude != null
                && latitude.compareTo(other.latitude) == 0
                && longitude.compareTo(other.longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name,
                latitude.stripTrailingZeros(),
                longitude.stripTrailingZeros()
        );
    }

}
