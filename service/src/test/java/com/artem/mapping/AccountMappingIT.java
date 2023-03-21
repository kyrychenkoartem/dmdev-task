package com.artem.mapping;

import com.artem.model.entity.Account;
import com.artem.model.entity.User;
import com.artem.model.type.AccountStatus;
import com.artem.model.type.Role;
import com.artem.model.type.UserStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountMappingIT extends MappingBaseEntity {

    @Test
    void checkAccountGet() {
        var user = getUser("Ivan", "Ivanov", "ivan@gmail.com");
        session.save(user);
        var expectedAccount = getAccount(user);
        session.save(expectedAccount);
        session.clear();

        var actualAccount = session.get(Account.class, expectedAccount.getId());

        assertThat(actualAccount).isEqualTo(expectedAccount);
    }

    @Test
    void checkAccountInsert() {
        var user = getUser("Petr", "Petrov", "petr@gmail.com");
        session.save(user);
        var expectedAccount = getAccount(user);
        session.save(expectedAccount);
        session.clear();

        var actualAccount = session.get(Account.class, expectedAccount.getId());

        assertThat(actualAccount.getId()).isNotNull();
    }

    @Test
    void checkAccountUpdate() {
        var user = getUser("Petr", "Petrov", "petr@gmail.com");
        session.save(user);
        var expectedAccount = getAccount(user);
        session.save(expectedAccount);
        session.clear();
        expectedAccount.setStatus(AccountStatus.BLOCKED);
        session.update(expectedAccount);
        session.flush();
        session.clear();

        var actualAccount = session.get(Account.class, expectedAccount.getId());

        assertThat(actualAccount.getStatus()).isEqualTo(AccountStatus.BLOCKED);
    }

    @Test
    void checkAccountDelete() {
        var user = getUser("Petr", "Petrov", "petr@gmail.com");
        session.save(user);
        var expectedAccount = getAccount(user);
        session.save(expectedAccount);
        session.clear();
        session.delete(expectedAccount);
        session.flush();
        session.clear();

        var actualAccount = session.get(Account.class, expectedAccount.getId());

        assertThat(actualAccount).isNull();
    }

    private static User getUser(String firstname, String lastname, String email) {
        return User.builder()
                .firstName(firstname)
                .lastName(lastname)
                .email(email)
                .password("123")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .build();
    }

    private static Account getAccount(User user) {
        return Account.builder()
                .user(user)
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .createdBy(user.getEmail())
                .build();
    }
}
