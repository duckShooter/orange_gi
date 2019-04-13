package gi.orange.task;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() //I don't need this
			.sessionManagement().disable() //nor this
			.authorizeRequests() //Basic authorization [-H Authorization: Basic base64EncodedCredentials]
			.antMatchers("/login").permitAll() //Only this API call is permitted for all
			.anyRequest().authenticated(); //All other requests requires authentication
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//I'm not following Spring schema, I'm using my own; a single table that stores username and password only.  
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

