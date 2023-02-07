package com.artem.database;

import com.artem.model.entity.User;
import java.util.Optional;

public class UserRepository {

    public Optional<User> findByName(String name) {
        return Optional.of(new User(name, "lastName"));
    }
}
