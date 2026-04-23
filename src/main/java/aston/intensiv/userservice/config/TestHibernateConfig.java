package aston.intensiv.userservice.config;

import aston.intensiv.userservice.model.User;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;


public class TestHibernateConfig {

    public static SessionFactory buildSessionFactory(
            String jdbcUrl,
            String username,
            String password
    ) {
        Properties props = new Properties();
        props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.put("hibernate.connection.url", jdbcUrl);
        props.put("hibernate.connection.username", username);
        props.put("hibernate.connection.password", password);

        props.put("hibernate.hbm2ddl.auto", "create-drop");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");

        Configuration configuration = new Configuration();
        configuration.setProperties(props);
        configuration.addAnnotatedClass(User.class);

        return configuration.buildSessionFactory();
    }
}