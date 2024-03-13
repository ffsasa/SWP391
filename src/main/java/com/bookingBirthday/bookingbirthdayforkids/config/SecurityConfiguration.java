package com.bookingBirthday.bookingbirthdayforkids.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {


    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/account/register").permitAll()
                        .requestMatchers("/api/account/customer/authenticate").permitAll()
                        .requestMatchers("/api/account/admin/authenticate").permitAll()
                        .requestMatchers("/api/account/host/authenticate").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/account/test").permitAll()
                        .requestMatchers("/api/account/test1").permitAll()
                        .requestMatchers("/api/account/signin/gmail").permitAll()
                        .requestMatchers("/api/package/get-all").permitAll()
                        .requestMatchers("/api/package/get-id/{id}").permitAll()
                        .requestMatchers("/api/packageService/get-all").permitAll()
                        .requestMatchers("/api/packageService/get-id/{id}").permitAll()
                        .requestMatchers("/api/payment-method/getAll-payment-method").permitAll()
                        .requestMatchers("/api/payment-method/getId-payment-method/{id}").permitAll()
                        .requestMatchers("/api/services/getAll-service").permitAll()
                        .requestMatchers("/api/services/getId-service/{id}").permitAll()
                        .requestMatchers("/api/theme/get-all").permitAll()
                        .requestMatchers("/api/theme/get-id/{id}").permitAll()
                        .requestMatchers("/api/venue/get-all").permitAll()
                        .requestMatchers("/api/venue/get-id/{id}").permitAll()
                        .requestMatchers("/api/party-booking/get-all").permitAll()
                        .requestMatchers("/api/partyDated/get-all").permitAll()
                        .requestMatchers("/api/slot//get-all").permitAll()
                        .requestMatchers("/slot-in-venue/get-all").permitAll()
                        .requestMatchers("/api/upgrade-service//get-all").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return  http.build();
    }
}
