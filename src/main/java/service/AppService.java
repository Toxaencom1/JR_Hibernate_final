package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CityDAO;
import dao.CountryDAO;
import entity.City;
import entity.Country;
import entity.CountryLanguage;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import redis.CityCountry;
import redis.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AppService {
    private final SessionFactory sessionFactory;
    private final RedisClient redisClient;
    private final CountryDAO countryDAO;
    private final CityDAO cityDAO;
    private final ObjectMapper mapper;

    public AppService(SessionFactory sessionFactory, RedisClient redisClient) {
        this.sessionFactory = sessionFactory;
        this.redisClient = redisClient;

        this.countryDAO = new CountryDAO(sessionFactory);
        this.cityDAO = new CityDAO(sessionFactory);
        this.mapper = new ObjectMapper();
    }

    public List<City> getAllCities() {
        try (Session session = sessionFactory.getCurrentSession()) {
            List<City> allCities = new ArrayList<>();
            Transaction transaction = session.beginTransaction();
            countryDAO.getAllCountries();

            int totalCount = cityDAO.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(cityDAO.getCitiesByRange(i, step));
            }
            transaction.commit();
            return allCities;
        }
    }

    public List<CityCountry> transformData(List<City> allCities) {
        return allCities.stream().map(city -> {
            Country country = city.getCountry();
            Set<CountryLanguage> countryLanguages = country.getLanguages();
            Set<Language> languages = countryLanguages.stream().map(lang -> Language.builder()
                    .language(lang.getLanguage())
                    .isOfficial(lang.getIsOfficial())
                    .percentage(lang.getPercentage())
                    .build()).collect(Collectors.toSet());
            return CityCountry.builder()
                    .id(city.getId())
                    .name(city.getName())
                    .district(city.getDistrict())
                    .population(city.getPopulation())
                    .countryCode(country.getCode())
                    .alternativeCountryCode(country.getCode2())
                    .countryName(country.getName())
                    .continent(country.getContinent())
                    .countryRegion(country.getRegion())
                    .countrySurfaceArea(country.getSurfaceArea())
                    .countryPopulation(country.getPopulation())
                    .languages(languages)
                    .build();
        }).toList();
    }


    public void pushToRedis(List<CityCountry> cityCountryList) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : cityCountryList) {
                try {
                    sync.set(String.valueOf(cityCountry.getId()), mapper.writeValueAsString(cityCountry));
                } catch (JsonProcessingException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
    }

    public void getRedisData(List<Integer> ids) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (Integer id : ids) {
                String value = sync.get(String.valueOf(id));
                try {
                    mapper.readValue(value, CityCountry.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
    }

    public void getMysqlData(List<Integer> ids) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            for (Integer id : ids) {
                City city = cityDAO.getById(id);
                Set<CountryLanguage> languages = city.getCountry().getLanguages();
            }
            session.getTransaction().commit();
        }
    }

    public void closeResources() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (redisClient != null) {
            redisClient.shutdown();
        }
    }
}
