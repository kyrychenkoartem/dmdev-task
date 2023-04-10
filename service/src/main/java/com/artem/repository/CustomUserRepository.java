package com.artem.repository;

import com.artem.model.dto.UserFilter;
import com.artem.model.entity.User;
import java.util.List;

public interface CustomUserRepository {

    void deleteUser(User entity);

    List<User> findAllByFilter(UserFilter filter);
}
