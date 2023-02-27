package com.artem.mapping;

import com.artem.model.entity.UtilityAccount;
import com.artem.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilityAccountMappingIT {

    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeAll
    static void init() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSession() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void closeSession() {
        session.getTransaction().rollback();
        session.close();
    }

    @AfterAll
    static void close() {
        sessionFactory.close();
    }

    @Test
    void checkUtilityAccountGet() {
        var expectedUtilityAccount = getUtilityAccount("112345654356", "Telus");
        session.save(expectedUtilityAccount);
        session.clear();

        var actualUtilityAccount = session.get(UtilityAccount.class, expectedUtilityAccount.getId());

        assertThat(actualUtilityAccount).isEqualTo(expectedUtilityAccount);
    }

    @Test
    void checkUtilityAccountInsert() {
        var expectedUtilityAccount = getUtilityAccount("23456543567654", "Koodo");
        session.save(expectedUtilityAccount);
        session.clear();

        var actualUtilityAccount = session.get(UtilityAccount.class, expectedUtilityAccount.getId());

        assertThat(actualUtilityAccount.getId()).isNotNull();
    }

    @Test
    void checkUtilityAccountUpdate() {
        var expectedUtilityAccount = getUtilityAccount("23456543567654", "Koodo");
        session.save(expectedUtilityAccount);
        session.clear();
        expectedUtilityAccount.setProviderName("Telus");
        session.update(expectedUtilityAccount);
        session.flush();
        session.clear();

        var actualUtilityAccount = session.get(UtilityAccount.class, expectedUtilityAccount.getId());

        assertThat(actualUtilityAccount.getProviderName()).isEqualTo("Telus");
    }

    @Test
    void checkUtilityAccountDelete() {
        var expectedUtilityAccount = getUtilityAccount("23456543567654", "Koodo");
        session.save(expectedUtilityAccount);
        session.clear();
        session.delete(expectedUtilityAccount);
        session.flush();
        session.clear();

        var actualUtilityAccount = session.get(UtilityAccount.class, expectedUtilityAccount.getId());

        assertThat(actualUtilityAccount).isNull();
    }

    private static UtilityAccount getUtilityAccount(String number, String provider) {
        return UtilityAccount.builder()
                .number(number)
                .providerName(provider)
                .build();
    }
}
