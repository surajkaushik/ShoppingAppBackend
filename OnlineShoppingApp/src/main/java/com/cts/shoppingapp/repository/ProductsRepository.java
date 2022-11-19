package com.cts.shoppingapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cts.shoppingapp.model.Products;

@Repository
public interface ProductsRepository extends MongoRepository<Products, Integer> {

	public List<Products> findByProductName(String productName);

	public Products findByProductId(Integer productId);

}
