package com.myserver.api.service.impl;

import com.google.gson.Gson;
import com.myserver.api.dao.GroupDAO;
import com.myserver.api.dao.ProductDAO;
import com.myserver.api.dto.EditModelDTO;
import com.myserver.api.dto.products.ProductDTO;
import com.myserver.api.dto.products.ProductResponseDTO;
import com.myserver.api.dto.products.SaveProductDTO;
import com.myserver.api.dto.response.MyServerResponse;
import com.myserver.api.model.Group;
import com.myserver.api.model.Product;
import com.myserver.api.service.ProductService;
import com.myserver.api.util.AESHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private GroupDAO groupDao;

    @Override
    public MyServerResponse save(EditModelDTO editModelDTO) {
        final String decodedProduct = AESHelper.decode(editModelDTO.getContent());
        final Gson gson = new Gson();
        final SaveProductDTO productDTO = gson.fromJson(decodedProduct, SaveProductDTO.class);
        if (productDTO.getPrice() < 0.0) {
            throw new RuntimeException("Ціна повинна бути більшою за 0");
        }
        final boolean exists = productDAO.exists(productDTO.getName(), 0);
        if (exists) {
            throw new RuntimeException("Товар уже існує із назвою " + productDTO.getName());
        }
        final ProductDTO savedProductDTO = productDAO.save(productDTO);
        final String json = gson.toJson(savedProductDTO);
        final String encode = AESHelper.encode(json);
        return new MyServerResponse(encode);
    }

    @Override
    public MyServerResponse update(EditModelDTO editModelDTO) {
        final String decodedJson = AESHelper.decode(editModelDTO.getContent());
        final Gson gson = new Gson();
        final Product decodedProduct = gson.fromJson(decodedJson, Product.class);
        if (decodedProduct.getPrice() < 0.0) {
            throw new RuntimeException("Ціна повинна бути більшою за 0");
        }
        final boolean exists = productDAO.exists(decodedProduct.getName(), decodedProduct.getId());
        if (exists) {
            throw new RuntimeException("Товар уже існує із назвою " + decodedProduct.getName());
        }
        productDAO.update(decodedProduct);
        return new MyServerResponse(200, "Status is OK");
    }

    @Override
    public MyServerResponse sell(Integer id, Integer count) {
        if (count < 0) {
            throw new RuntimeException("К-ть повинна бути більшою за 0");
        }
        productDAO.sell(id, count);
        return new MyServerResponse(200, "Status is OK");
    }

    @Override
    public MyServerResponse add(Integer id, Integer count) {
        if (count < 0) {
            throw new RuntimeException("К-ть повинна бути більшою за 0");
        }
        productDAO.add(id, count);
        return new MyServerResponse(200, "Status is OK");
    }

    @Override
    public MyServerResponse findAll() {
        final List<ProductDTO> products = productDAO.findAll();
        return getEncodedResponse(products);
    }

    @Override
    public MyServerResponse findByNameAndGroup(String name, Integer groupId) {
        final List<ProductDTO> products = productDAO.findByNameAndGroup("%" + name + "%", groupId);
        return getEncodedResponse(products);
    }

    @Override
    public MyServerResponse delete(Integer id) {
        productDAO.delete(id);
        return new MyServerResponse(200, "Ok");
    }

    private MyServerResponse getEncodedResponse(List<ProductDTO> products) {
        final List<Group> groups = groupDao.findAll();
        final int total = products.stream().mapToInt(ProductDTO::getCount).sum();
        final double totalPrice = products.stream().mapToDouble(s -> (s.getPrice() * s.getCount())).sum();
        final ProductResponseDTO responseDTO = new ProductResponseDTO(products, groups, total, totalPrice);
        final Gson gson = new Gson();
        final String json = gson.toJson(responseDTO);
        final String encode = AESHelper.encode(json);
        return new MyServerResponse(encode);
    }
}
