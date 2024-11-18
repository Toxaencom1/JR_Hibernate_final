package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Entity
@Table(name = "city")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

//    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    private String district;

    private Integer population;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof City)) return false;
        City city = (City) o;
        return Objects.equals(getId(), city.getId()) && Objects.equals(getName(), city.getName()) && Objects.equals(getDistrict(), city.getDistrict()) && Objects.equals(getPopulation(), city.getPopulation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDistrict(), getPopulation());
    }
}
