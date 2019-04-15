package gi.orange.task.controllers;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import gi.orange.task.models.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//IT
@RunWith(SpringRunner.class)
@SpringBootTest //Full auto-configuration, spring security is up, database is up
public class AuthControllerIT {
	@Autowired
	private AuthController authController;
	
	@Test
	public void doLogin_withValidCredentials_shouldGetOk() throws Exception {
		User user = new User("user", "secret");
		MockMvc mvc = MockMvcBuilders.standaloneSetup(authController).build();
		
		mvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(user)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void doLogin_withInvalidCredentials_shouldGetNotFound() throws Exception {
		User user = new User("user", "wrong");
		MockMvc mvc = MockMvcBuilders.standaloneSetup(authController).build();
		
		mvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(user)))
				.andExpect(status().isUnauthorized());
	}
}
