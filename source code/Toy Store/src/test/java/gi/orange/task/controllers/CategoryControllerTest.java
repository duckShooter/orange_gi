package gi.orange.task.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import gi.orange.task.error.RestExceptionHandler;
import gi.orange.task.models.Category;
import gi.orange.task.models.Product;
import gi.orange.task.services.CategoryService;
import gi.orange.task.services.ProductService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolationException;

/* Testing against: HTTP response status, response content-type header, response body, exceptions
 * Verifying the number of interactions with service methods to be sure we're in the correct end-point and it's executed as intended 
 * Actually most of these tests would make more sense for unit testing the service itself not the controller			
 * As for the controller what I'm interested in here is the testing payload (i.e. its correctness), not the information it carries.
*/ 

@WebMvcTest //This doesn't bring up Spring Security
public class CategoryControllerTest {

	@Mock
	private CategoryService categoryService;
	
	@Mock
	private ProductService productService;
	
	@InjectMocks
	private CategoryController categoryController; //Test subject
	
	private MockMvc mockMvc;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController).
				setControllerAdvice(new RestExceptionHandler()).build(); //Using my own exception handler 
	}

	@Test
	public void getCategories_WhenEmpty_shouldGetOKAndEmptyBody() throws Exception {
		//given
		when(categoryService.findAll()).thenReturn(new HashSet<>()); //Simulate empty table
		
		//when
		mockMvc.perform(get("/api/categories"))
		
		//then
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0))); //Empty Json object
		verify(categoryService).findAll(); //Called once and only once
	}
	
	@Test
	public void getCategories_whenExist_ShouldGetOkAndBody() throws Exception {
		//given
		final String name = "puzzels";
		Set<Category> categories = new HashSet<>(); //Stored in database
		Category category = new Category();
		category.setName(name);
		categories.add(category);
		
		when(categoryService.findAll()).thenReturn(categories);
		
		//when
		mockMvc.perform(get("/api/categories"))
		
		//then
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name", is(name))); //Verify the received serialized object
		verify(categoryService).findAll();
	}
	
	@Test
	public void addCategory_withAllRequiredFieldsSupplied_shouldGetCreatedAndBody() throws Exception {
		//given
		final String name = "educational";
		Category categorySent = new Category(); //Sent by client
		categorySent.setName(name); //Required
		
		Category categoryReceived = new Category(); //Produced by server
		categoryReceived.setId(5); //Generated
		categoryReceived.setName(name);
		
		
		when(categoryService.save(Mockito.any(Category.class))).thenReturn(categoryReceived);
		
		//when
		mockMvc.perform(put("/api/categories")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(categorySent)))
		
		//then
			.andExpect(status().isCreated())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.name", is(name)))
			.andExpect(jsonPath("$", hasKey("id")));
		verify(categoryService).save(Mockito.any(Category.class));
	}
	
	@Test
	public void addCategory_withRequiredFieldsMissing_shouldGetUnprocessableEntity() throws Exception {
		//given
		when(categoryService.save(Mockito.any(Category.class))).thenThrow(ConstraintViolationException.class);
		
		//when
		mockMvc.perform(put("/api/categories")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(new Category())))
		//then
			.andExpect(status().isUnprocessableEntity());
		verify(categoryService).save(Mockito.any(Category.class));
	}
	
	@Test
	public void getCategroy_withValidId_shouldGetOkAndBody() throws Exception {
		//given
		final Integer id = 10;
		Category category = new Category();
		category.setId(10);
		category.setName("board games");
		
		when(categoryService.findById(anyInt())).thenReturn(category);
		
		//when
		mockMvc.perform(get("/api/categories/" + id))
		
		//then
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.id", is(id)))
			.andExpect(jsonPath("$", hasKey("name")));
		verify(categoryService).findById(id);
	}
	
	@Test
	public void getCategory_withInvalidId_shouldGetNotFound() throws Exception {
		//given
		when(categoryService.findById(anyInt())).thenReturn(null);
		
		//when
		mockMvc.perform(get("/api/categories/" + anyInt()))
		
		//then
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$").doesNotExist());
		verify(categoryService).findById(anyInt());
	}
	
	@Test
	public void deleteCategory_withValidId_shouldGetNoContent() throws Exception {
		//given
		doNothing().when(categoryService).deleteById(anyInt());
		
		//when
		mockMvc.perform(delete("/api/categories/" + anyInt())) //Pretend that any id is valid
		
		//then
			.andExpect(status().isNoContent())
			.andExpect(jsonPath("$").doesNotExist());
		verify(categoryService).deleteById(anyInt());
	}
	
	@Test
	public void deleteCategory_withInvalidId_shouldGetUnprocessableEntity() throws Exception {
		//given
		doThrow(EmptyResultDataAccessException.class).when(categoryService).deleteById(anyInt());
		
		//when
		mockMvc.perform(delete("/api/categories/" + anyInt())) //Pretend that any id is invalid
		
		//then
			.andExpect(status().isUnprocessableEntity());
		verify(categoryService).deleteById(anyInt());
	}
	
	@Test
	public void addProduct_withValidCategoryIdAndAllRequiredFieldsSupplied_shouldGetCreatedAndBody() throws Exception {
		//given
		final String name = "pistol";
		final String description = "pew pew";
		final String vendor = "pistol shop";
		final BigDecimal price = BigDecimal.TEN;
		final Integer categoryId = 11;
		
		Product productSent = new Product();
		productSent.setName(name); //Required
		productSent.setDescription(description);
		productSent.setVendor(vendor);
		productSent.setPrice(price);
		
		Category category = new Category();
		category.setId(categoryId);
		category.setName("weapons");
		
		Product productReceived = new Product();
		productReceived.setId(1); //Generated
		productReceived.setName(name);
		productReceived.setDescription(description);
		productReceived.setVendor(vendor);
		productReceived.setPrice(price);
		productReceived.setCategory(category);
		
		when(categoryService.findById(anyInt())).thenReturn(category);
		when(productService.save(Mockito.any(Product.class))).thenReturn(productReceived);
		
		//when
		mockMvc.perform(put("/api/categories/" + categoryId + "/")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(productSent)))
		
		//then
		.andExpect(status().isCreated())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(jsonPath("$", hasKey("id")))
		.andExpect(jsonPath("$", hasKey("category")))
		.andExpect(jsonPath("$.category.id", is(categoryId)));
		verify(categoryService).findById(categoryId);
		verify(productService).save(Mockito.any(Product.class));
	}
	
	@Test
	public void addProduct_withInvalidCategoryId_shouldGetNotFound() throws Exception {
		//given
		Product productSent = new Product();
		productSent.setName("pistol");
		productSent.setDescription("pew pew pew");
		productSent.setVendor("ammo nation");
		productSent.setPrice(BigDecimal.TEN);
		
		when(categoryService.findById(anyInt())).thenReturn(null);
		
		//when
		mockMvc.perform(put("/api/categories/" + anyInt() + "/")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(productSent)))
		
		//then
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$").doesNotExist());
		verify(categoryService).findById(anyInt());
		verifyZeroInteractions(productService);
	}
}
