package gi.orange.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import gi.orange.task.models.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
}
