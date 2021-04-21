package com.myserver.api.dao.impl;

import com.myserver.api.config.DatabaseConfig;
import com.myserver.api.dao.ProductDAO;
import com.myserver.api.dto.products.ProductDTO;
import com.myserver.api.dto.products.SaveProductDTO;
import com.myserver.api.model.Product;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Service
public class ProductDAOImpl implements ProductDAO {

    @Override
    public ProductDTO save(SaveProductDTO productDTO) {
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("insert into products (name, description, producer, count, price, group_id) values (?,?,?,?,?,?)");
            preparedStatement.setString(1, productDTO.getName());
            preparedStatement.setString(2, productDTO.getDescription());
            preparedStatement.setString(3, productDTO.getProducer());
            preparedStatement.setInt(4, productDTO.getCount());
            preparedStatement.setDouble(5, productDTO.getPrice());
            preparedStatement.setInt(6, productDTO.getGroupId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (!resultSet.isClosed()) {
                final int id = resultSet.getInt(1);
                return findById(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Product product) {
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("update products set name = ?, description = ?, producer = ?, count = ?, price = ?, group_id = ? where id = ?");
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setString(3, product.getProducer());
            preparedStatement.setInt(4, product.getCount());
            preparedStatement.setDouble(5, product.getPrice());
            preparedStatement.setDouble(6, product.getGroupId());
            preparedStatement.setInt(7, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sell(Integer id, int count) {
        final ProductDTO productDTO = findById(id);
        if (count > productDTO.getCount()) {
            throw new RuntimeException("К-ть більша за доступну");
        }
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("update products set count = ? where id = ?");
            preparedStatement.setInt(1, productDTO.getCount() - count);
            preparedStatement.setInt(2, productDTO.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Integer id, int count) {
        final ProductDTO productDTO = findById(id);
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("update products set count = ? where id = ?");
            preparedStatement.setInt(1, productDTO.getCount() + count);
            preparedStatement.setInt(2, productDTO.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ProductDTO> findAll() {
        List<ProductDTO> products = new LinkedList<>();
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("select p.id, p.name, p.description, p.producer, p.count, p.price, g.name as product_group from products p, groups g where p.group_id = g.id order by p.id desc");
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final ProductDTO product = getProduct(resultSet);
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public ProductDTO findById(Integer id) {
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("select p.id, p.name, p.description, p.producer, p.count, p.price, g.name as product_group from products p, groups g where p.group_id = g.id and p.id = ?");
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isClosed()) {
                return getProduct(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ProductDTO> findByNameAndGroup(String name, Integer groupId) {
        List<ProductDTO> products = new LinkedList<>();
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("select p.id, p.name, p.description, p.producer, p.count, p.price, g.name as product_group from products p, groups g where p.group_id = g.id and ((? = 0 or g.id = ?) and (? = '' or upper(p.name) like upper(?))) order by p.id desc");
            preparedStatement.setInt(1, groupId);
            preparedStatement.setInt(2, groupId);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, name);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final ProductDTO product = getProduct(resultSet);
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public boolean exists(String name, Integer id) {
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("select p.id from products p where p.name = ? and p.id <> ?;");
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
    public void deleteByGroupId(Integer id) {
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("delete from products where group_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            final Connection connection = DatabaseConfig.connection;
            final PreparedStatement preparedStatement = connection.prepareStatement("delete from products where id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ProductDTO getProduct(ResultSet resultSet) throws SQLException {
        final int productId = resultSet.getInt("id");
        final String name = resultSet.getString("name");
        final int count = resultSet.getInt("count");
        final double price = resultSet.getDouble("price");
        final String description = resultSet.getString("description");
        final String producer = resultSet.getString("producer");
        final String group = resultSet.getString("product_group");
        return new ProductDTO(productId, name, description, producer, count, price, group);
    }
}
