package telran.java38.security.configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

//@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityAuthorizationConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(HttpMethod.POST, "/account/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic();
		http.csrf().disable();
		http.cors();
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/forum/post/{id}/**")
				.permitAll()
			.antMatchers("/account/{login}/role/**")
				.hasRole("ADMINISTRATOR")
			.antMatchers(HttpMethod.PUT, "/account/{login}/**","/account/{login}/password/**")
				.access("#login == authentication.name")
			.antMatchers(HttpMethod.DELETE, "/account/{login}/**")
				.access("#login == authentication.name  or hasRole('ADMINISTRATOR')")
			.antMatchers(HttpMethod.PUT, "/forum/post/{id}/like/**")
				.authenticated()
			.antMatchers(HttpMethod.POST, "/forum/post/{author}/**")
				.access("#author == authentication.name")
			.antMatchers(HttpMethod.PUT, "/forum/post/{id}/comment/{author}/**")
				.access("#author == authentication.name")
			.antMatchers("/forum/post/{id}/**")
				.access("@customSecurity.checkPostAuthority(#id, authentication.name) or hasRole('MODERATOR')")
			.anyRequest()
				.authenticated();
	}
}
