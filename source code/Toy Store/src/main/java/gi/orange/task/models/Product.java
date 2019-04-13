package gi.orange.task.models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Product extends BaseEntity {
	
	@NotNull //JSR 303
	@NotEmpty
	private String name;
	
	private String description;
	
	private String vendor;
	
	@Column(precision=10, scale=2)
	private BigDecimal price;
	
	@ManyToOne
	@JoinColumn(nullable=false) //JPA
	private Category category;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", description=" + description + ", vendor=" + vendor + ", price="
				+ price + ", category=" + category.getName() + ", id=" + id + "]";
	}
}
