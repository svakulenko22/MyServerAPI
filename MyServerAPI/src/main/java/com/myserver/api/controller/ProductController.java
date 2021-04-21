package com.myserver.api.controller;

import com.myserver.api.auth.TokenHandler;
import com.myserver.api.dto.EditModelDTO;
import com.myserver.api.dto.response.MyServerResponse;
import com.myserver.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goods")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private TokenHandler tokenHandler;

    @PostMapping
    public ResponseEntity<MyServerResponse> save(@RequestHeader("Authorization") String token, @RequestBody EditModelDTO editModelDTO) {
        tokenHandler.parseUserFromToken(token);
        final MyServerResponse response = productService.save(editModelDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<MyServerResponse> update(@RequestHeader("Authorization") String token, @RequestBody EditModelDTO editModelDTO) {
        tokenHandler.parseUserFromToken(token);
        final MyServerResponse response = productService.update(editModelDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/sell")
    public ResponseEntity<MyServerResponse> release(@RequestParam Integer id, @RequestParam Integer count) {
        final MyServerResponse response = productService.sell(id, count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<MyServerResponse> add(@RequestParam Integer id, @RequestParam Integer count) {
        final MyServerResponse response = productService.add(id, count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<MyServerResponse> findAll(@RequestHeader("Authorization") String token) {
        tokenHandler.parseUserFromToken(token);
        final MyServerResponse response = productService.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<MyServerResponse> findByNameAndGroup(@RequestHeader("Authorization") String token, @RequestParam String name, @RequestParam Integer groupId) {
        tokenHandler.parseUserFromToken(token);
        final MyServerResponse response = productService.findByNameAndGroup(name, groupId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MyServerResponse> delete(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        tokenHandler.parseUserFromToken(token);
        final MyServerResponse response = productService.delete(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
