package gi.orange.task.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import gi.orange.task.models.User;

@Controller
public class AuthController {
	AuthenticationManager authenticationManager;
	
	public AuthController(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	/* 
	 * What's going on? (This is an unusual way to do authentication so I had to explain)
	 * ----------------------------------------------------------------------------------
	 * What happens here is we're cheating the HTTP basic authentication sequence.
	 * Instead of sending authentication header with username:password, we send the username
	 * and password in the request body and manually ask spring to authenticate this info.
	 * 
	 * This is actually what Spring does under the hood but it first extracts the info from the
	 * Authentication header decode it and checks against the info in the database, in this 
	 * case we're providing the login info (pre-decoded) directly and Spring does the checking part. 
	 * On successful authentication Spring will add a 'set-cookie' header with a generated token 
	 * associated with the logged in user to be sent on every request instead of 
	 * (or along side with) the Authentication header. Spring then uses this cookie value
	 * sent in the 'cookie' header with every request to authorize the user. 
	 */
	@CrossOrigin("http://localhost:4200")
	@PostMapping(value="/login", produces=MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> doLogin(@RequestBody User user, HttpServletRequest req) {
		//Authentication with (username/password) as (principal/credentials)
		UsernamePasswordAuthenticationToken upat = 
				new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		SecurityContext securityContext = SecurityContextHolder.getContext();
		//Authenticate and save the information in the current execution thread
		securityContext.setAuthentication(authenticationManager.authenticate(upat));
		return ResponseEntity.ok().build();
	}
}
