package gi.orange.task;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		 * [Issue]
		 * What's going on? (REST is being violated, that's what's going on)  
		 * -----------------------------------------------------------------
		 * Since Spring respond to HTTP basic authentication with a generated token stored as a cookie
		 * this introduces a state into a stateless protocol.
		 * Spring requires that either the 'cookie' header or 'authorization' header to be present in
		 * order to authenticate the client.
		 * If the client has disabled the use of cookies then the 'authorization' header must be present
		 * this is actually what we want, to use the 'authorization' header instead of 'cookie'.
		 * The code of line I've used `sessionManagement().disable()` doesn't seem to do the trick!
		 * This remains an open issue for me until i fix it.
		 * -----------------------------------------------------------------
		 * [Fix]
		 * while using `sessionManagement().disable()` didn't work as I expected, changing the
		 * creation policy of sessions to stateless did the job.
		 * However, this makes the login API useless, since any request with a correct authorization
		 * header will allow access to resources without the need to login.
		 * So we will think of login as a way of granting an access token (the session id in this case)
		 * and the 'cookie' header as the access token used on every request.
		 * -----------------------------------------------------------------
		 * [Conclusion]
		 * Having a "RESTful" login API doesn't make sense when using HTTP basic authentication
		 */
		http
			.csrf().disable() //I don't need this
			.cors().and() //Use default CORS configuration to handle pre-flight requests 
			.sessionManagement().disable() //I don't need this
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests() //Basic authorization [-H Authorization: Basic base64EncodedCredentials]
			.antMatchers("/login").permitAll() //Only this API call is permitted for all
			.anyRequest().authenticated() //All other requests requires authentication
			.and().httpBasic();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//I'm not following Spring schema, I'm using my own; a single table that stores username and password only (fits the purpose).
		auth.jdbcAuthentication().dataSource(dataSource) //I used JDBC directly instead of implementing JPA authentication provider
			.usersByUsernameQuery("SELECT username, password, TRUE FROM user WHERE username=?") //All accounts are enabled
			.authoritiesByUsernameQuery("SELECT username, 'ROLE_USER' from user WHERE username=?"); //I've only one role defined
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(); //Using bcrypt hashing
	}
	
	@Bean 
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean(); //I need it to perform manual authentication
	}
}

