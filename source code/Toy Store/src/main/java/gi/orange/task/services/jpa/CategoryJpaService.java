package gi.orange.task.services.jpa;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import gi.orange.task.models.Category;
import gi.orange.task.repositories.CategoryRepository;
import gi.orange.task.services.CategoryService;

@Service
@Profile({"default", "jpa"}) //I'm planning to provide another implementation with only JDBC if the time allows
public class CategoryJpaService implements CategoryService {

	private CategoryRepository categoryRepository;
	
	public CategoryJpaService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Set<Category> findAll() {
		Set<Category> categories = new HashSet<>();
		categoryRepository.findAll().forEach(categories::add);
		return categories;
	}

	@Override
	public Category findById(Integer id) {
		return categoryRepository.findById(id).orElse(null);
	}

	@Override
	public Category save(Category object) {
		return categoryRepository.save(object);
	}

	@Override
	public void delete(Category object) {
		categoryRepository.delete(object);
	}

	@Override
	public void deleteById(Integer id) {
		categoryRepository.deleteById(id);
	}
}
