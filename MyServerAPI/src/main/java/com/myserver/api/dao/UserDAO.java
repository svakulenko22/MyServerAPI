package com.myserver.api.dao;

import com.myserver.api.model.User;

public interface UserDAO {

    User findOne(Integer id);

    User findByLogin(String login);
}
