package com.artem.dao;

import com.artem.util.HibernateTestUtil;
import com.artem.util.TestDataImporter;
import java.lang.reflect.Proxy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class RepositoryTestBase {

    private static SessionFactory sessionFactory;
    protected static Session session;

    @BeforeAll
    static void init() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        TestDataImporter.importData(sessionFactory);
        session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
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
        sessionFactory.close();
    }
}
