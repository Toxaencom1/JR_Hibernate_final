package entity;

import enums.Continent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "country")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    @Column(name = "code_2")
    private String code2;

    private String name;

    @Enumerated(EnumType.ORDINAL)
    private Continent continent;

    private String region;

    @Column(name = "surface_area")
    private BigDecimal surfaceArea;

    @Column(name = "indep_year")
    private Short independenceYear;

    private Integer population;

    @Column(name = "life_expectancy")
    private BigDecimal lifeExpectancy;

    private BigDecimal gnp;

    @Column(name = "gnpo_id")
    private BigDecimal gnpoId;

    @Column(name = "local_name")
    private String localName;

    @Column(name = "government_form")
    private String governmentForm;

    @Column(name = "head_of_state")
    private String headOfState;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capital")
    private City capital;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Set<CountryLanguage> languages;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Country)) return false;
        Country country = (Country) o;
        return Objects.equals(getId(), country.getId()) && Objects.equals(getCode(), country.getCode()) && Objects.equals(getCode2(), country.getCode2()) && Objects.equals(getName(), country.getName()) && getContinent() == country.getContinent() && Objects.equals(getRegion(), country.getRegion()) && Objects.equals(getSurfaceArea(), country.getSurfaceArea()) && Objects.equals(getIndependenceYear(), country.getIndependenceYear()) && Objects.equals(getPopulation(), country.getPopulation()) && Objects.equals(getLifeExpectancy(), country.getLifeExpectancy()) && Objects.equals(getGnp(), country.getGnp()) && Objects.equals(getGnpoId(), country.getGnpoId()) && Objects.equals(getLocalName(), country.getLocalName()) && Objects.equals(getGovernmentForm(), country.getGovernmentForm()) && Objects.equals(getHeadOfState(), country.getHeadOfState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCode(), getCode2(), getName(), getContinent(), getRegion(), getSurfaceArea(), getIndependenceYear(), getPopulation(), getLifeExpectancy(), getGnp(), getGnpoId(), getLocalName(), getGovernmentForm(), getHeadOfState());
    }
}
