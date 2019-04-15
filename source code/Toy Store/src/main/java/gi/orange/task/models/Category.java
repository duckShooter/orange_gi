package gi.orange.task.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Category extends BaseEntity {
	@NotNull
	@NotEmpty
	private String name;
	
	@JsonBackReference
	@OneToMany(mappedBy="category", cascade=CascadeType.ALL)
	private Set<Product> products = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "Category [name=" + name + ", products=" + products + ", id=" + id + "]";
	}
}
