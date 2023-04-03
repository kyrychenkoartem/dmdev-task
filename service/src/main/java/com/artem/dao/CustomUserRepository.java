package com.artem.dao;

import com.artem.model.entity.User;

public interface CustomUserRepository {

    void deleteUser(User entity);
}
