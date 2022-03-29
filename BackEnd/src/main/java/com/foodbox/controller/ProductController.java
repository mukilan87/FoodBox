package com.foodbox.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.foodbox.model.Admin;
import com.foodbox.model.Product;
import com.foodbox.repository.AdminRepository;
import com.foodbox.repository.ProductRepository;

import com.foodbox.exception.ResourceNotFoundException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ProductController {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired AdminRepository adminRepository;

	@GetMapping("/products/Admin")
	public List<Product> getAdminProducts() {
		return productRepository.findAll();
	}

	@GetMapping("/products/cust")
	public List<Product> getAllProducts() {
		List<Product> prodList=productRepository.findIfAvail();
		if(prodList.isEmpty()) {
			List<Admin> adminList = adminRepository.findAll();
			if(adminList.isEmpty()) {
				adminRepository.save(new Admin("admin","password"));
			}
			addProdIfEmpty(new Product(1,"Chole Bhature","combination of chana masala and bhatura/puri, a fried bread made from maida","NorthIndian",100,0,0,"yes","./assets/images/northindian/cholebhature.jpg"));
			addProdIfEmpty(new Product(2,"Butter Chicken","Type of curry made from chicken with a spiced tomato and butter (makhan) sauce","NorthIndian",250,5,0,"yes","./assets/images/northindian/butterchicken.jpg"));
			addProdIfEmpty(new Product(3,"Leche Frita","mixture of milk, flour, and sugar that's coated in eggs and flour and then fried in olive oil","Chinese",150,2,0,"yes","./assets/images/chinese/lechesfrita.jpg"));
			addProdIfEmpty(new Product(4,"Salmorejo","Made of tomato, bread, extra virgin olive oil and garlic","Chinese",95,5,0,"yes","./assets/images/chinese/salmorejo.jpg"));
			addProdIfEmpty(new Product(5,"Hydrabed Briyani","made with basmati rice and meat (mostly chicken, Lamb Meat)","SouthIndian",435,0,0,"yes","./assets/images/southindian/hydrabedbriyani.jpg"));
			addProdIfEmpty(new Product(6,"Masala Dosa","a thin (usually crispy) flat bread originating from South India, made from a fermented batter predominantly consisting of lentils and rice","SouthIndian",150,0,0,"yes","./assets/images/southindian/masaladosa.jpg"));
			addProdIfEmpty(new Product(7,"Tandoori"," a dish of roasted chicken marinated in yogurt and generously spiced, giving the meat its trademark red colour","Punjabi",200,8,0,"yes","./assets/images/punjabi/tandoori.jpg"));
			addProdIfEmpty(new Product(8,"Lassi","a creamy, frothy yogurt-based drink, blended with water and various fruits or seasonings","Punjabi",110,3,0,"yes","./assets/images/punjabi/lassi.jpg"));
			prodList=productRepository.findIfAvail();
		}
		return prodList;
	}
	
	public void addProdIfEmpty(Product product) {
		int min = 10000;
		int max = 99999;
		int b = (int) (Math.random() * (max - min + 1) + min);
		product.setId(b);
		float temp = (product.getActualPrice()) * (product.getDiscount() / 100);
		float price = product.getActualPrice() - temp;
		product.setPrice(price);
		productRepository.save(product);
	}

	@PostMapping("/products")
	public Product addProduct(@RequestBody Product product) {
		int min = 10000;
		int max = 99999;
		int b = (int) (Math.random() * (max - min + 1) + min);
		product.setId(b);
		float temp = (product.getActualPrice()) * (product.getDiscount() / 100);
		float price = product.getActualPrice() - temp;
		product.setPrice(price);
		return productRepository.save(product);
	}
	
	@PutMapping("/products/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails){
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee Not Found with " + id));
		product.setName(productDetails.getName());
		product.setDesc(productDetails.getDesc());
		product.setCategory(productDetails.getCategory());
		product.setImagepath(productDetails.getImagepath());
		product.setActualPrice(productDetails.getActualPrice());
		product.setDiscount(productDetails.getDiscount());
		product.setAvail(productDetails.getAvail());
		float temp = (product.getActualPrice()) * (product.getDiscount() / 100);
		float price = product.getActualPrice() - temp;
		product.setPrice(price);
		
		Product updatedProd = productRepository.save(product);
		return ResponseEntity.ok(updatedProd);
		
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteProduct(@PathVariable Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee Not Found with " + id));
		productRepository.delete(product);
		Map<String, Boolean> map = new HashMap<>();
		map.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(map);
	}

	@GetMapping("products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Found with " + id));
		return ResponseEntity.ok(product);
	}

	@GetMapping("products/search/{keyword}")
	public List<Product> getSearchProducts(@PathVariable String keyword) {
		return productRepository.homeSearch(keyword);
	}

	@GetMapping("products/chinese")
	public List<Product> getChinese() {
		return productRepository.getChinese();
	}

	@GetMapping("products/southindian")
	public List<Product> getSouthindian() {
		return productRepository.getSouthindian();
	}

	@GetMapping("products/northindian")
	public List<Product> getNorthindian() {
		return productRepository.getNorthindian();
	}

	@GetMapping("products/punjabi")
	public List<Product> getPunjabi() {
		return productRepository.getPunjabi();
	}
}