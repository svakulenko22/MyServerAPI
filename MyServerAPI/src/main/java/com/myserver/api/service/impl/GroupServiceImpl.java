package com.myserver.api.service.impl;

import com.google.gson.Gson;
import com.myserver.api.dao.GroupDAO;
import com.myserver.api.dao.ProductDAO;
import com.myserver.api.dto.EditModelDTO;
import com.myserver.api.dto.response.MyServerResponse;
import com.myserver.api.model.Group;
import com.myserver.api.service.GroupService;
import com.myserver.api.util.AESHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDAO groupDao;
    @Autowired
    private ProductDAO productDAO;

    @Override
    public MyServerResponse save(EditModelDTO editModelDTO) {
        final String decodedName = AESHelper.decode(editModelDTO.getContent());
        if ("".equals(decodedName)) {
            throw new RuntimeException("Назва групи пуста");
        }
        final boolean exists = groupDao.exists(decodedName, 0);
        if (exists) {
            throw new RuntimeException("Група уже існує із назвою " + decodedName);
        }
        final Group group = groupDao.save(decodedName);
        final Gson gson = new Gson();
        final String json = gson.toJson(group);
        final String encode = AESHelper.encode(json);
        return new MyServerResponse(encode);
    }

    @Override
    public MyServerResponse update(EditModelDTO editModelDTO) {
        final String decodedGroup = AESHelper.decode(editModelDTO.getContent());
        final Gson gson = new Gson();
        final Group group = gson.fromJson(decodedGroup, Group.class);
        if ("".equals(group.getName())) {
            throw new RuntimeException("Назва групи пуста");
        }
        final boolean exists = groupDao.exists(group.getName(), group.getId());
        if (exists) {
            throw new RuntimeException("Група уже існує із назвою " + group.getName());
        }
        groupDao.update(group);
        return new MyServerResponse(200, "Status is OK");
    }

    @Override
    public MyServerResponse findAll() {
        final List<Group> groups = groupDao.findAll();
        final Gson gson = new Gson();
        final String json = gson.toJson(groups);
        final String encode = AESHelper.encode(json);
        return new MyServerResponse(encode);
    }

    @Override
    public MyServerResponse delete(Integer id) {
        productDAO.deleteByGroupId(id);
        groupDao.delete(id);
        return new MyServerResponse(200, "Ok");
    }
}
