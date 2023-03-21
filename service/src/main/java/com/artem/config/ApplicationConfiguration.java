package com.artem.config;

import com.artem.util.HibernateUtil;
import java.lang.reflect.Proxy;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.artem")
public class ApplicationConfiguration {

    @Bean
    org.hibernate.cfg.Configuration configuration() {
        return HibernateUtil.buildConfiguration();
    }

    @Bean
    SessionFactory buildSessionFactory() {
        var configuration = configuration();
        configuration.configure();
        return configuration().buildSessionFactory();
    }

    @Bean
    EntityManager entityManager() {
        var sessionFactory = buildSessionFactory();
        return (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
    }
}
