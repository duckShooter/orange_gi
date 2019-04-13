package gi.orange.task.controllers;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gi.orange.task.models.Category;
import gi.orange.task.models.Product;
import gi.orange.task.services.CategoryService;
import gi.orange.task.services.ProductService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	
	private CategoryService categoryService;
	private ProductService productService;

	public CategoryController(CategoryService categoryService, ProductService productService) {
		this.categoryService = categoryService;
		this.productService = productService;
	}

	@GetMapping(value= {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
	public Set<Category> listAllCategories() {
		return categoryService.findAll();
	}
	
	@PutMapping(value = {"/", ""},  consumes= MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Category addCategory(@RequestBody Category category) {
		return categoryService.save(category);
	}
	
	@GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Category getCategory(@PathVariable Integer id) {
		return categoryService.findById(id);
	}
	
	@PutMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Product addNewProductToCategory(@PathVariable Integer id, @RequestBody Product product) {
		product.setCategory(categoryService.findById(id));
		return productService.save(product);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void deleteCategory(@PathVariable Integer id) {
		categoryService.deleteById(id);
	}
	
	@GetMapping(value="/{id}/products", produces = MediaType.APPLICATION_JSON_VALUE)
	public Set<Product> listAllProductsInCategory(@PathVariable Integer id) {
		return categoryService.findById(id).getProducts();
	}
	
}
