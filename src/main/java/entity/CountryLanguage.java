package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "country_language")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ToString.Exclude
    @ManyToOne
    private Country country;

    private String language;

    @Column(name = "is_official", columnDefinition = "BIT")
    private Boolean isOfficial;

    private BigDecimal percentage;
}
