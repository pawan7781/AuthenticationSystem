package com.becoder.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	OAuthAuthenticationSuccessHandler handler;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService getDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public DaoAuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
//		http.csrf().disable()
//		.authorizeHttpRequests().requestMatchers("/","/register","/signin", "/verify").permitAll()
//		.anyRequest().authenticated().and()
//		.formLogin().loginPage("/signin").loginProcessingUrl("/userLogin")
//		//.usernameParameter("email")
//		.defaultSuccessUrl("/dashboard").permitAll();


		http.csrf(csrf -> csrf.disable()) // Disable CSRF
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/register", "/signin", "/verify", "/forgot", "/send-otp","/verify-otp", "/verifyOTP", "/forgot_Email_form", "/password_change_form", "/change-password").permitAll() // Public endpoints
						.anyRequest().authenticated() // All other endpoints require authentication
				)
				.formLogin(form -> form
						.loginPage("/signin") // Custom login page
						.loginProcessingUrl("/userLogin") // URL to process login
						//.usernameParameter("email") // Uncomment if using email as the username field
						.defaultSuccessUrl("/dashboard", true) // Redirect to dashboard after successful login
						.permitAll()
				);

		http.oauth2Login(oauth -> {
			oauth.loginPage("/login");
			oauth.successHandler(handler);
		});

		http.logout(logoutForm -> {
			logoutForm.logoutUrl("/do-logout");
			logoutForm.logoutSuccessUrl("/login?logout=true");
		});

		return http.build();


	}

}
