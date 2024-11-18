package config;


import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import jakarta.persistence.Entity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.*;
import org.reflections.Reflections;


public class DataBaseConfig {

    public SessionFactory buildSessionFactory() {
        Configuration cfg = new Configuration();
        cfg.setProperty(JdbcSettings.JAKARTA_JDBC_URL, "jdbc:mysql://localhost:3307/world")
                .setProperty(JdbcSettings.DIALECT, "org.hibernate.dialect.MySQLDialect")
                .setProperty(JdbcSettings.JAKARTA_JDBC_DRIVER, "com.mysql.cj.jdbc.Driver")
                .setProperty(JdbcSettings.JAKARTA_JDBC_USER, "")
                .setProperty(JdbcSettings.JAKARTA_JDBC_PASSWORD, "")
                .setProperty(JdbcSettings.SHOW_SQL, "true")
                .setProperty(JdbcSettings.FORMAT_SQL, "true")
                .setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread")
                .setProperty(Environment.STATEMENT_BATCH_SIZE, "100")
                .setProperty("hibernate.hbm2ddl.auto", "validate");
        Reflections reflections = new Reflections("entity");
        reflections.getTypesAnnotatedWith(Entity.class).forEach(cfg::addAnnotatedClass);
        return cfg.buildSessionFactory();
    }

    public RedisClient redisClientPrepareConfig() {
        return RedisClient.create(RedisURI.create("localhost", 6379));
    }
}
