package com.artem.mapping;

import com.artem.model.entity.User;
import com.artem.model.type.Role;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMappingIT extends MappingBaseEntity {

    @Test
    void checkUserGet() {
        var user = getUser("Ivan", "Ivanov", "ivan@gmail.com");
        session.save(user);
        session.clear();

        var actualUser = session.get(User.class, user.getId());

        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    void checkUserInsert() {
        var user = getUser("Ivan", "Ivanov", "ivan@gmail.com");
        session.save(user);
        session.clear();

        var actualUser = session.get(User.class, user.getId());

        assertThat(actualUser.getId()).isNotNull();
    }

    @Test
    void checkUserUpdate() {
        var user = getUser("Ivan", "Ivanov", "ivan@gmail.com");
        session.save(user);
        session.clear();
        user.setFirstName("Petr");
        user.setLastName("Petrov");
        session.update(user);
        session.flush();
        session.clear();

        var actualUser = session.get(User.class, user.getId());

        assertThat(actualUser.getFirstName()).isEqualTo("Petr");
        assertThat(actualUser.getLastName()).isEqualTo("Petrov");
    }

    @Test
    void checkUserDelete() {
        var user = getUser("Ivan", "Ivanov", "ivan@gmail.com");
        session.save(user);
        session.clear();
        session.delete(user);
        session.flush();
        session.clear();

        var actualUser = session.get(User.class, user.getId());

        assertThat(actualUser).isNull();

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
}
