import config.DataBaseConfig;
import entity.City;
import io.lettuce.core.RedisClient;
import lombok.Getter;
import org.hibernate.SessionFactory;
import redis.CityCountry;
import service.AppService;

import java.util.List;

@Getter
public class Main {
    private final DataBaseConfig dataBaseConfig;
    private final SessionFactory sessionFactory;
    private final RedisClient redisClient;
    private final AppService service;

    public Main() {
        this.dataBaseConfig = new DataBaseConfig();
        this.sessionFactory = dataBaseConfig.buildSessionFactory();
        this.redisClient = dataBaseConfig.redisClientPrepareConfig();

        this.service = new AppService(sessionFactory, redisClient);
    }


    public static void main(String[] args) {
        Main main = new Main();
        AppService service = main.getService();
        List<City> cities = service.getAllCities();
        System.out.println("=".repeat(100));

        List<CityCountry> cityCountryList = service.transformData(cities);
        service.pushToRedis(cityCountryList);

        main.getSessionFactory().getCurrentSession().close();

        List<Integer> ids = List.of(576, 1255, 362, 18, 1456, 20, 2773, 3098, 1, 4002);

        long startRedis = System.currentTimeMillis();
        service.getRedisData(ids);
        long stopRedis = System.currentTimeMillis();

        long startMysql = System.currentTimeMillis();
        service.getMysqlData(ids);
        long stopMysql = System.currentTimeMillis();

        System.out.printf("%s: %d ms\n", "Redis", (stopRedis - startRedis));
        System.out.printf("%s: %d ms\n", "MySQL", (stopMysql - startMysql));

        service.closeResources();
    }
}
