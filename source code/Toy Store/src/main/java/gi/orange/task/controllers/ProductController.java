package gi.orange.task.controllers;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import gi.orange.task.models.Product;
import gi.orange.task.services.ProductService;

@Controller //@RestController is redundant here, as I'll be using ResponseEntity to get better control of what's returned
@RequestMapping("/api/products")
public class ProductController {
	
	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping(value={"", "/"}, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<Product> getAllProducts() {
		return productService.findAll();
	}
	
	@GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> getProduct(@PathVariable Integer id) {
		Product product = productService.findById(id);
		return product != null ?
				new ResponseEntity<>(product, HttpStatus.OK) : ResponseEntity.notFound().build();
	}
	
	@PutMapping(value= {"", "/"}, consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code=HttpStatus.CREATED)
	public @ResponseBody Product addProduct(@RequestBody Product product) {
		product.setId(null); //Defensive
		return productService.save(product);			

	}

	/* Multiple scenarios here (i.e.full or partial update ...) I'd rather deal with this using DTOs, backing beans 
	 * with converters or mappers but I'll spare the extra layer and keep it simple with null-check festival */
	@PostMapping(value={"/{id}"}, consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE) //or Patch?
	public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
		Product updatedProduct = productService.updateProduct(id, product);
		return updatedProduct != null ?
				new ResponseEntity<>(updatedProduct, HttpStatus.OK) : ResponseEntity.notFound().build();
	} 
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable Integer id) {
		productService.deleteById(id);
	}
}
