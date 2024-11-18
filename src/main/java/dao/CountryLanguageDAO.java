package dao;

import entity.Country;
import entity.CountryLanguage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CountryLanguageDAO {
    private final SessionFactory sessionFactory;

    public List<CountryLanguage> getAllCountryLanguages() {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            Query<CountryLanguage> countryLanguageQuery = session.createQuery("from CountryLanguage", CountryLanguage.class);
            List<CountryLanguage> countryLanguageList = countryLanguageQuery.list();
            transaction.commit();
            return countryLanguageList;
        }
    }
}
