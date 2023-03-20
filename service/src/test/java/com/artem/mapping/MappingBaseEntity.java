package com.artem.mapping;

import com.artem.config.ApplicationConfiguration;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
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
        sessionFactory = (SessionFactory) context.getBean("buildSessionFactory");
    }

    @BeforeEach
    void openSession() {
        session = (Session) context.getBean("entityManager");
        session.beginTransaction();
    }

    @AfterEach
    void closeSession() {
        session.getTransaction().rollback();
    }

    @AfterAll
    static void close() {
        sessionFactory.close();
        context.close();
    }
}
