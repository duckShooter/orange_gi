package gi.orange.task.controllers;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gi.orange.task.models.Product;
import gi.orange.task.services.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping(value={"", "/"}, produces=MediaType.APPLICATION_JSON_VALUE)
	public Set<Product> listAllProducts() {
		return productService.findAll();
	}
	
	@GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Product getProduct(@PathVariable Integer id) {
		return productService.findById(id);
	}
	
	@PutMapping(value= {"", "/"}, produces=MediaType.APPLICATION_JSON_VALUE)
	public Product addProduct(@RequestBody Product product) {
		if(product.getCategory() == null || product.getCategory().getId() == null)
			throw new RuntimeException("category id is required");
		return productService.save(product);
	}
	
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable Integer id) {
		productService.deleteById(id);
	}

	//We can daydream in multiple scenarios here (i.e. is it partial or full update? Will aggregations be updated too? ... etc)
	//and use DTOs, backing beans with converter or mappers but I'll keep it simple and do the null-check festival
	@PostMapping(value={"/{id}"})
	public Product updateProduct(@PathVariable Integer id, @RequestBody Product product) {
		return productService.updateProduct(id, product);			
	} 
}
