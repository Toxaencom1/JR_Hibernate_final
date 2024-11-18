package dao;

import entity.City;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CityDAO {

    private final SessionFactory sessionFactory;

    public List<City> getCitiesByRange(int offset, int limit) {
        Query<City> cityQuery = sessionFactory.getCurrentSession().createQuery("from City", City.class);
        cityQuery.setFirstResult(offset);
        cityQuery.setMaxResults(limit);
        return cityQuery.list();
    }

    public int getTotalCount() {
        Query<Long> query = sessionFactory.getCurrentSession().createQuery("select count(c) from City c", Long.class);
        return Math.toIntExact(query.uniqueResult());
    }

    public City getById(Integer id) {
        Query<City> query = sessionFactory.getCurrentSession().createQuery("select c from City c join fetch c.country where c.id = :ID", City.class);
        query.setParameter("ID", id);
        return query.getSingleResult();
    }
}
