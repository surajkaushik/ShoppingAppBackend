package com.cts.shoppingapp.configuration.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cts.shoppingapp.Exception.ProductNotFoundException;
import com.cts.shoppingapp.model.Products;
import com.cts.shoppingapp.repository.ProductsRepository;


@Service
public class ProductsServiceImpl {

	@Autowired
	private ProductsRepository productsRepository;

	public List<Products> getAllProducts() {
		List<Products> productsList = productsRepository.findAll();
		if (productsList.size() > 0) {
			return productsList;
		} else
			throw new ProductNotFoundException("Oops! No Products Available !!");
	}

	public String addProducts(Products product) {
		productsRepository.save(product);
		return "Product Added successfully !!";
	}

	public List<Products> getProductsByName(String productName) {
		List<Products> productList = productsRepository.findAll();
		if (productList.size() > 0) {
			
			List<Products> newProductList=productList.stream().filter((p)->p.getProductName().equals(productName)).collect(Collectors.toList());

			return newProductList;
		} else
			throw new ProductNotFoundException("No such product available !!");
	}

	public String deleteProduct(Integer productId) {

		Optional<Products> opt = productsRepository.findById(productId);

		if (opt.isPresent()) {
			Products prod = opt.get();
			productsRepository.delete(prod);
			return "Product deleted from catalog";
		} else
			throw new ProductNotFoundException("Product Id is not available !!");
	}

	public Products updateProductStatus(Integer productId) {
		Products product = productsRepository.findByProductId(productId);

		if (product.getQuantity() > 0) {
			product.setProductStatus("Hurry Up to Purchase");
			productsRepository.save(product);
			return product;
		} else {
			product.setProductStatus("Out Of stock");
			productsRepository.save(product);
			return product;
		}

	}

	public Integer getProductQuantity(Integer id) {
		Products product = productsRepository.findByProductId(id);
		if (product.getQuantity() > 0) {
			return product.getQuantity();
		}else
			throw new ProductNotFoundException("PRODUCT OUT OF STOCK");
	}
}
