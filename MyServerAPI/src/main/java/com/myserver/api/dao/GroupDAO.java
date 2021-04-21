package com.myserver.api.dao;

import com.myserver.api.model.Group;

import java.util.List;

public interface GroupDAO {

    Group save(String name);

    void update(Group group);

    List<Group> findAll();

    boolean exists(String name, Integer id);

    void delete(Integer id);
}
