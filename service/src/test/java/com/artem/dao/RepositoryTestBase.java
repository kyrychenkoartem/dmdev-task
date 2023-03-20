package com.artem.dao;

import com.artem.config.ApplicationConfiguration;
import com.artem.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class RepositoryTestBase {

    private static SessionFactory sessionFactory;
    protected static Session session;
    protected static AnnotationConfigApplicationContext context;

    @BeforeAll
    static void init() {
        context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        sessionFactory = (SessionFactory) context.getBean("buildSessionFactory");
        TestDataImporter.importData(sessionFactory);
        session = (Session) context.getBean("entityManager");
    }

    @BeforeEach
    void openSession() {
        session.beginTransaction();
    }

    @AfterEach
    void closeSession() {
        session.getTransaction().rollback();
    }

    @AfterAll
    static void close() {
        context.close();
        sessionFactory.close();
    }
}
