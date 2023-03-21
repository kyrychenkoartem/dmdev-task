package com.artem.mapping;

import com.artem.config.ApplicationConfiguration;

import javax.annotation.PreDestroy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class MappingBaseEntity {

    private static SessionFactory sessionFactory;
    protected Session session;
    protected static AnnotationConfigApplicationContext context;

    @BeforeAll
    static void init() {
        context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        sessionFactory = context.getBean("buildSessionFactory", SessionFactory.class);
    }

    @BeforeEach
    void openSession() {
        session = context.getBean("entityManager", Session.class);
        session.beginTransaction();
    }

    @AfterEach
    void closeSession() {
        session.getTransaction().rollback();
    }

    @PreDestroy
    void closeContext() {
        context.close();
        sessionFactory.close();
    }
}
