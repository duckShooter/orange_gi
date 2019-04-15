package gi.orange.task.services.jpa;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import gi.orange.task.models.Category;
import gi.orange.task.models.Product;
import gi.orange.task.repositories.CategoryRepository;
import gi.orange.task.repositories.ProductRepository;
import gi.orange.task.services.ProductService;

@Service
@Profile({"default", "jpa"}) //I'm planning to provide another implementation with only JDBC if the time allows
public class ProductJpaService implements ProductService {
	
	private ProductRepository productRepositroy;
	private CategoryRepository categoryRepository;

	public ProductJpaService(ProductRepository productRepositroy, CategoryRepository categoryRepository) {
		this.productRepositroy = productRepositroy;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Set<Product> findAll() {
		Set<Product> products = new HashSet<>();
		productRepositroy.findAll().forEach(products::add); //List-to-set
		return products;
	}

	@Override
	public Product findById(Integer id) {
		return productRepositroy.findById(id).orElse(null);
	}

	@Override
	public Product save(Product object) {
		return productRepositroy.save(object);
	}

	@Override
	public void delete(Product object) {
		productRepositroy.delete(object);
	}

	@Override
	public void deleteById(Integer id) {
		productRepositroy.deleteById(id);
	}

	@Override
	public Set<Product> findAllInCategory(String categoryName) { //Search by name may or may not intersect multiple categories 
		Set<Product> productsInCategories = new HashSet<>();
		Set<Category> categories = categoryRepository.findByNameContainingIgnoreCase(categoryName);
		if(categories != null && !categories.isEmpty())
			categories.forEach(category -> {
				category.getProducts().forEach(productsInCategories::add);
			});
		return productsInCategories;
	}

	@Override
	public Set<Product> findAllInCategory(Integer categoryId) {
		Optional<Category> category = categoryRepository.findById(categoryId);
		if(category.isPresent())
			return category.get().getProducts();
		return null;
	}
	
	@Override 
	public Product updateProduct(Integer id, Product modifiedProduct) {
		Optional<Product> optionalProduct = productRepositroy.findById(id);
		if(optionalProduct.isPresent()) {
			Product updatedProduct = optionalProduct.get();
			
			//NullnEmpty checks! not a problem for a light weight object. 
			if(!isNullOrEmpty(modifiedProduct.getName()))
				updatedProduct.setName(modifiedProduct.getName());
			if(!isNullOrEmpty(modifiedProduct.getDescription()))
				updatedProduct.setDescription(modifiedProduct.getDescription());
			if(!isNullOrEmpty(modifiedProduct.getVendor()))
				updatedProduct.setVendor(modifiedProduct.getVendor());
			if(modifiedProduct.getPrice() != null)
				updatedProduct.setPrice(modifiedProduct.getPrice());
			
			save(updatedProduct);
			return updatedProduct;
		}
		return null;
	}
	
	private boolean isNullOrEmpty(String string) { //A small helper function for strings
		return string == null || string.isEmpty();
	}
}
