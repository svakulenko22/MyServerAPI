package com.myserver.api.service;

import com.myserver.api.dto.EditModelDTO;
import com.myserver.api.dto.response.MyServerResponse;

public interface ProductService {

    MyServerResponse save(EditModelDTO editModelDTO);

    MyServerResponse update(EditModelDTO editModelDTO);

    MyServerResponse sell(Integer id, Integer count);

    MyServerResponse add(Integer id, Integer count);

    MyServerResponse findAll();

    MyServerResponse findByNameAndGroup(String name, Integer groupId);

    MyServerResponse delete(Integer id);
}
