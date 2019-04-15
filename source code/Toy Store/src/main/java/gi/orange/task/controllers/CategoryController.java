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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import gi.orange.task.models.Category;
import gi.orange.task.models.Product;
import gi.orange.task.services.CategoryService;
import gi.orange.task.services.ProductService;

@Controller
@RequestMapping("/api/categories")
public class CategoryController {
	
	private CategoryService categoryService;
	private ProductService productService;

	public CategoryController(CategoryService categoryService, ProductService productService) {
		this.categoryService = categoryService;
		this.productService = productService;
	}

	@GetMapping(value={"/", ""}, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<Category> listAllCategories() {
		return categoryService.findAll();
	}
	
	@PutMapping(value={"/", ""}, consumes=MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code=HttpStatus.CREATED)
	public @ResponseBody Category addCategory(@RequestBody Category category) {
		category.setId(null); //Defensive (Wouldn't be the case if I'm using DTOs)
		return categoryService.save(category);			
	}
	
	@GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Category> getCategory(@PathVariable Integer id) {
		Category category =  categoryService.findById(id);
		return category != null ? new ResponseEntity<>(category, HttpStatus.OK)
				: ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void deleteCategory(@PathVariable Integer id) {
			categoryService.deleteById(id);
	}
	
	@PostMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Category> renameCategory(@PathVariable Integer id, @RequestParam String name) {
		Category category = categoryService.findById(id);
		if(category == null)
			return ResponseEntity.notFound().build();
		if(name != null && !name.isEmpty()) //If name is missing or empty, nothing change
			category.setName(name);
		return new ResponseEntity<>(categoryService.save(category), HttpStatus.OK);
	}
	
	@PutMapping(value="/{id}", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> addProductToCategory(@PathVariable Integer id, @RequestBody Product product) {
		product.setId(null); //Defensive
		Category category = categoryService.findById(id);
		if(category == null)
			return ResponseEntity.notFound().build();
		product.setCategory(category);
		return new ResponseEntity<Product>(productService.save(product), HttpStatus.CREATED);			
	}
	
	@GetMapping(value="/{id}/products", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Product>> getAllProductsInCategory(@PathVariable Integer id) {
		Category category = categoryService.findById(id);
		return category != null ? new ResponseEntity<>(category.getProducts(), HttpStatus.OK)
				: ResponseEntity.notFound().build();
	}
}
