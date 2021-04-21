package com.myserver.api.dao;

import com.myserver.api.dto.products.ProductDTO;
import com.myserver.api.dto.products.SaveProductDTO;
import com.myserver.api.model.Product;

import java.util.List;

public interface ProductDAO {

    ProductDTO save(SaveProductDTO productDTO);

    void update(Product product);

    void sell(Integer id, int count);

    void add(Integer id, int count);

    List<ProductDTO> findAll();

    ProductDTO findById(Integer id);

    List<ProductDTO> findByNameAndGroup(String name, Integer groupId);

    boolean exists(String name, Integer id);

    void deleteByGroupId(Integer id);

    void delete(Integer id);
}
