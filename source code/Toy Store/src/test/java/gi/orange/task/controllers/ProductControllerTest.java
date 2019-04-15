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
import gi.orange.task.services.ProductService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/* Testing against: HTTP response status, response content-type header, response body, exceptions
 * verifying the number of interactions with service methods to be sure we're in the correct end-point and it's executed as intended 
 * Actually most of these tests would make more sense for unit testing the service itself not the controller			
 * As for the controller what I'm interested in here is the testing payload (i.e. its correctness), not the information it carries.
 */
@WebMvcTest
public class ProductControllerTest {

	@Mock
	private ProductService productService;
	
	@InjectMocks
	private ProductController productController; //Test subject
	
	private MockMvc mockMvc;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(productController)
				.setControllerAdvice(new RestExceptionHandler()).build();
	}

	@Test
	public void getProducts_whenExist_shouldGetOkAndBody() throws Exception {
		//given
		Set<Product> products = new HashSet<>(); //Stored in database
		Category category = new Category();
		category.setId(1);
		category.setName("all");
		Product product = new Product();
		product.setId(1);
		product.setCategory(category);
		products.add(product);
		product = new Product();
		product.setId(2);
		product.setCategory(category);
		products.add(product);
		
		when(productService.findAll()).thenReturn(products);
		
		//when
		mockMvc.perform(get("/api/products/"))
		
		//then
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$", hasSize(2)));
		verify(productService).findAll();	
	}

	@Test
	public void getProducts_whenEmpty_shouldGetOkAndEmptyBody() throws Exception {
		//given
		when(productService.findAll()).thenReturn(new HashSet<>()); //Simulate empty table
		
		//when
		mockMvc.perform(get("/api/products/"))
		
		//then
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$", hasSize(0)));
		verify(productService).findAll();	
	}

	@Test
	public void updateProduct_withValidId_shouldGetOkAndBody() throws Exception {
		//given
		final Integer productId = 1;
		final String productName = "updated product";
		Product product = new Product(); //sent by client
		product.setName(productName);
		product.setPrice(BigDecimal.TEN);
		
		Product updatedProduct = new Product(); //produced by server
		updatedProduct.setId(productId);
		updatedProduct.setName(productName);
		updatedProduct.setPrice(BigDecimal.TEN);
		
		when(productService.updateProduct(anyInt(), Mockito.any(Product.class))).thenReturn(updatedProduct);
		
		mockMvc.perform(post("/api/products/" + productId)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(product)))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$", hasKey("id"))) //Expect an updated product
			.andExpect(jsonPath("$.id", is(productId))) //with the same as id in the path
			.andExpect(jsonPath("$.name", is(productName))) //and the name is updated
			.andExpect(jsonPath("$.price", is(10))); //and the price is updated
		verify(productService).updateProduct(anyInt(), Mockito.any(Product.class));
	}
	
	@Test
	public void updateProduct_withInValidId_shouldGetNotFound() throws Exception {
		//given
		Product product = new Product(); //sent by client
		product.setName("product");
		product.setPrice(BigDecimal.TEN);
		when(productService.updateProduct(anyInt(), Mockito.any(Product.class))).thenReturn(null);
		
		//when
		mockMvc.perform(post("/api/products/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(product)))
			.andExpect(status().isNotFound());
		verify(productService).updateProduct(anyInt(), Mockito.any(Product.class));
	}
	
	@Test
	public void deleteProduct_withValidId_shouldGetNoContent() throws Exception {
		//given
		doNothing().when(productService).deleteById(anyInt());
		
		//when
		mockMvc.perform(delete("/api/products/" + anyInt()))
		
		//then
			.andExpect(status().isNoContent());
		verify(productService).deleteById(anyInt());
	}
	
	@Test
	public void deleteProduct_withInValidId_shouldUnprocessableEntity() throws Exception {
		//given
		doThrow(EmptyResultDataAccessException.class).when(productService).deleteById(anyInt());
		
		//when
		mockMvc.perform(delete("/api/products/" + anyInt()))
		
		//then
			.andExpect(status().isUnprocessableEntity());
		verify(productService).deleteById(anyInt());
	}
}
