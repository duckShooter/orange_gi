package gi.orange.task.services;

import java.util.Set;

import gi.orange.task.models.Product;

public interface ProductService extends BaseService<Product, Integer> {
	Set<Product> findAllInCategory(String categoryName);
	Set<Product> findAllInCategory(Integer categoryId);
	Product updateProduct(Integer id, Product modifiedProduct);
}
