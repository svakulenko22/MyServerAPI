package com.myserver.api.dao.impl;

import com.myserver.api.config.DatabaseConfig;
import com.myserver.api.dao.GroupDAO;
import com.myserver.api.model.Group;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Service
public class GroupDAOImpl implements GroupDAO {

    @Override
    public Group save(String name) {
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("insert into groups (name) values (?)");
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (!resultSet.isClosed()) {
                final int id = resultSet.getInt(1);
                return new Group(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Group group) {
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("update groups set name = ? where id = ?");
            preparedStatement.setString(1, group.getName());
            preparedStatement.setInt(2, group.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Group> findAll() {
        List<Group> groups = new LinkedList<>();
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("select * from groups order by id desc");
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final Group group = getGroup(resultSet);
                groups.add(group);
            }
            return groups;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    @Override
    public boolean exists(String name, Integer id) {
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("select g.id from groups g where g.name = ? and g.id <> ?;");
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            return !resultSet.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void delete(Integer id) {
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("delete from groups where id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Group getGroup(ResultSet resultSet) throws SQLException {
        final int id = resultSet.getInt("id");
        final String name = resultSet.getString("name");
        return new Group(id, name);
    }
}
