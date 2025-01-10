package com.certificate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.certificate.Models.Cardentials;
import com.certificate.Services.AdminService;
import com.certificate.Services.CardentialsService;
import com.certificate.security.JWT.JwtRequestFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    
    @Autowired
    private CardentialsService cardentialsService;
    
    @Autowired
    private AdminService adminService;
    

    

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for stateless APIs
            .authorizeHttpRequests(auth -> auth                                                               
                .requestMatchers("/main/register","/main/authenticate","/uploads/**","/main/authenticate-Admin","/main/changeusername/**","/main/changepassword/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless session management
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWT filter before the username/password filter

        return http.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://certificate-sih1622.up.railway.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Apply CORS settings to all endpoints
        return source;
    }
    
    
    @Bean
    protected UserDetailsService userDetailsService() {
        return username -> {
        	
        	
        	if (adminService.getUsername().equalsIgnoreCase(username)) {
				 return org.springframework.security.core.userdetails.User
		                    .withUsername(adminService.getUsername())
		                    .password(adminService.getPassword())
		                    .roles("ADMIN")
		                    .build();
			} else {

				
				 Cardentials cardentials = cardentialsService.findByUserEmailOrPhone(username)
		                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
		            return org.springframework.security.core.userdetails.User
		                    .withUsername(cardentials.getUserEmailOrPhone())
		                    .password(cardentials.getUserPassword())
		                    .roles("USER")
		                    .build();
			
				
			}
        	
				
				 
        	
          
           
        };
    }


    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
