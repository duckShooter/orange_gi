package gi.orange.task.services;

import java.util.Set;

import gi.orange.task.models.Category;

public interface CategoryService extends BaseService<Category, Integer> {
	Set<Category> findByName(String name);
}