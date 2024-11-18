package dao;

import entity.Country;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CountryDAO {
    private final SessionFactory sessionFactory;

    public List<Country> getAllCountries() {
        Query<Country> countryQuery = sessionFactory.getCurrentSession().createQuery("from Country c left join fetch c.languages", Country.class);
        return countryQuery.list();
    }
}
