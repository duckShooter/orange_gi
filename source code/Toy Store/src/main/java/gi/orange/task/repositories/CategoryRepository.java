package gi.orange.task.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import gi.orange.task.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Set<Category> findByNameIgnoreCase();

}
