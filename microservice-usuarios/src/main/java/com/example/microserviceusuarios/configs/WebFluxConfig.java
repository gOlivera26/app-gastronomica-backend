package com.example.microserviceusuarios.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") // Or use "*" to allow all origins
                .allowedMethods("*") // Or specify which methods to allow: .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*"); // Or specify which headers to allow: .allowedHeaders("Authorization", "Content-Type")
    }
}
