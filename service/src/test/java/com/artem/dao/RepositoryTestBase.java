package com.artem.dao;

import com.artem.config.TestApplicationConfiguration;
import com.artem.util.TestDataImporter;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class RepositoryTestBase {

    private static SessionFactory sessionFactory;
    protected static EntityManager session;
    protected static AnnotationConfigApplicationContext context;

    @BeforeAll
    static void init() {
        context = new AnnotationConfigApplicationContext(TestApplicationConfiguration.class);
        sessionFactory = context.getBean("sessionFactory", SessionFactory.class);
        TestDataImporter.importData(sessionFactory);
        session = context.getBean("entityManager", EntityManager.class);
    }

    @BeforeEach
    void openSession() {
        session.getTransaction().begin();
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
