package com.myserver.api.controller;

import com.myserver.api.auth.TokenHandler;
import com.myserver.api.dto.EditModelDTO;
import com.myserver.api.dto.response.MyServerResponse;
import com.myserver.api.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private TokenHandler tokenHandler;

    @PostMapping
    public ResponseEntity<MyServerResponse> save(@RequestHeader("Authorization") String token, @RequestBody EditModelDTO editModelDTO) {
        tokenHandler.parseUserFromToken(token);
        final MyServerResponse response = groupService.save(editModelDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<MyServerResponse> update(@RequestHeader("Authorization") String token, @RequestBody EditModelDTO editModelDTO) {
        tokenHandler.parseUserFromToken(token);
        final MyServerResponse response = groupService.update(editModelDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<MyServerResponse> findAll(@RequestHeader("Authorization") String token) {
        tokenHandler.parseUserFromToken(token);
        final MyServerResponse response = groupService.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MyServerResponse> delete(@PathVariable Integer id) {
        final MyServerResponse response = groupService.delete(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
