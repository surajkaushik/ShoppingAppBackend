package com.cts.shoppingapp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.shoppingapp.configuration.services.ProductsServiceImpl;
import com.cts.shoppingapp.model.Products;


@RestController
@Validated
@RequestMapping("/api/v1.0/shopping")
public class ProductsController {

	@Autowired
	private ProductsServiceImpl productsService;

	// For All
	@GetMapping("/all")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<Products> getAllProducts() {
		return productsService.getAllProducts();
	}

	// For All
	@GetMapping("/product/search/{productName}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<List<Products>> getProductByName(@PathVariable("productName") String productName) {
		
		List<Products> prod = productsService.getProductsByName(productName.toLowerCase());
		return new ResponseEntity<List<Products>>(prod, HttpStatus.FOUND);
	}
	

	// For Admin Only
	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> addProducts(@Valid @RequestBody Products product) {
		
		String addmsg = productsService.addProducts(product);
		return new ResponseEntity<String>(addmsg, HttpStatus.CREATED);
	}

	// For Admin only
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") Integer id) {

		String res = productsService.deleteProduct(id);
		return new ResponseEntity<String>(res, HttpStatus.OK);
	}
	// For Admin Only
	@GetMapping("/product/search/quantity/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Integer> getProductQuantity(@PathVariable("id") Integer id) {
		
		Integer quantity = productsService.getProductQuantity(id);
		return new ResponseEntity<Integer>(quantity, HttpStatus.FOUND);
	}


	// For Admin only
	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Products> updateProductStatus(@PathVariable("id") Integer id) {

		Products prod1 = productsService.updateProductStatus(id);
		return new ResponseEntity<Products>(prod1, HttpStatus.OK);
	}

}
