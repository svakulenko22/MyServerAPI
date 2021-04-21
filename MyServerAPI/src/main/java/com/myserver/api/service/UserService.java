package com.myserver.api.service;

import com.myserver.api.dto.response.AuthResponse;
import com.myserver.api.model.User;

public interface UserService {

    User findOne(Integer id);

    AuthResponse login(String login, String password);
}
