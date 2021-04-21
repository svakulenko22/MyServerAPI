package com.myserver.api.dto.products;

import com.myserver.api.model.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ProductResponseDTO {
    private List<ProductDTO> products;
    private List<Group> groups;
    private Integer total;
    private Double totalPrice;
}
