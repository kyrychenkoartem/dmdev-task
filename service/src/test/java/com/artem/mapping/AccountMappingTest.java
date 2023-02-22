package com.artem.mapping;

import com.artem.model.entity.Account;
import com.artem.model.entity.User;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.Role;
import com.artem.util.HibernateUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountMappingTest extends MappingTestBase {

    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeAll
    static void init() {
        sessionFactory = HibernateUtil.buildSessionFactory();
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
    void checkAccountGet() {
        var user = getUser("Ivan", "Ivanov", "ivan@gmail.com");
        session.save(user);
        var expectedAccount = getAccount(user.getId());
        session.save(expectedAccount);
        session.clear();

        var actualAccount = session.get(Account.class, expectedAccount.getId());

        assertThat(actualAccount).isEqualTo(expectedAccount);
    }

    @Test
    void checkAccountInsert() {
        var user = getUser("Petr", "Petrov", "petr@gmail.com");
        session.save(user);
        var expectedAccount = getAccount(user.getId());
        session.save(expectedAccount);
        session.clear();

        var actualAccount = session.get(Account.class, expectedAccount.getId());

        assertThat(actualAccount.getId()).isNotNull();
    }

    private static User getUser(String firstname, String lastname, String email) {
        return User.builder()
                .firstName(firstname)
                .lastName(lastname)
                .email(email)
                .password("123")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .build();
    }

    private static Account getAccount(Long userId) {
        return Account.builder()
                .user(userId)
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .createdBy(userId.toString())
                .build();
    }
}
