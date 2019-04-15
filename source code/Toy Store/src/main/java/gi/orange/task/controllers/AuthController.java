package gi.orange.task.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import gi.orange.task.models.User;

@Controller
public class AuthController {
	
	AuthenticationManager authenticationManager;
	
	public AuthController(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@PostMapping(value="/login", produces=MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> doLogin(@RequestBody User user, HttpServletRequest req) {
		try {
			//Authentication with (username/password) as (principal/credentials)
			UsernamePasswordAuthenticationToken upat = 
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			SecurityContext securityContext = SecurityContextHolder.getContext();
			//Authenticate and save the information in the current execution thread
			securityContext.setAuthentication(authenticationManager.authenticate(upat));
			return ResponseEntity.ok().build();
		} catch (AuthenticationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
