package aston.intensiv.userservice.config;

import aston.intensiv.userservice.model.User;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfig {

    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            configuration.addAnnotatedClass(User.class);

            return configuration.buildSessionFactory();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания SessionFactory", e);
        }
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

}

