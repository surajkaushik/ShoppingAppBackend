package com.cts.shoppingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
	private Integer productId;
	private String productName;
    private Double price;
    private Integer quantity;

}
