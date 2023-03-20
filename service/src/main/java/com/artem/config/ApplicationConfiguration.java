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
    public static SessionFactory buildSessionFactory() {
        org.hibernate.cfg.Configuration configuration = HibernateUtil.buildConfiguration();
        configuration.configure();
        return configuration.buildSessionFactory();
    }

    @Bean
    public EntityManager entityManager() {
        var sessionFactory = HibernateUtil.buildSessionFactory();
        return (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
    }
}
