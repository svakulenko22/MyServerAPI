package com.myserver.api.dto.products;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SaveProductDTO {
    private String name;
    private String description;
    private String producer;
    private Integer count;
    private Double price;
    private Integer groupId;
}
