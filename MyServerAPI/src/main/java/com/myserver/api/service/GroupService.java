package com.myserver.api.service;

import com.myserver.api.dto.EditModelDTO;
import com.myserver.api.dto.response.MyServerResponse;

public interface GroupService {

    MyServerResponse save(EditModelDTO editModelDTO);

    MyServerResponse update(EditModelDTO editModelDTO);

    MyServerResponse findAll();

    MyServerResponse delete(Integer id);
}
