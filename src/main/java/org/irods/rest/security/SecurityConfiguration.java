package org.irods.rest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String[] AUTH_WHITELIST = {
			// -- swagger ui
			"/api-docs", "/swagger-resources", "/swagger-resources/**", "/configuration/ui", "/configuration/security",
			"/swagger-ui.html", "/webjars/**"
			// other public endpoints of your API may be appended to this array
	};

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll()
				.antMatchers("/**").authenticated().and().addFilter(new JwtAuthorizationFilter(authenticationManager()))
				.addFilter(new IrodsBasicAuthenticationFilter(authenticationManager()))
				.addFilter(new IrodsTicketAuthenticationFilter(authenticationManager())).sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		/*
		 * http.cors().and().csrf().disable().authorizeRequests().antMatchers(
		 * AUTH_WHITELIST).permitAll()
		 * .antMatchers("/**").authenticated().and().addFilter(new
		 * JwtAuthorizationFilter(authenticationManager()))
		 * .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		 * 
		 */

		/*
		 * 
		 * 
		 * .ignoring().antMatchers("/v2/api-docs", "/configuration/ui",
		 * "/swagger-resources/**", "/configuration/security", "/swagger-ui.html",
		 * "/webjars/**");
		 */
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

}
