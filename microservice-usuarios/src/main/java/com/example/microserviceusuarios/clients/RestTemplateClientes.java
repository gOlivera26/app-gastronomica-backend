package com.example.microserviceusuarios.clients;

import com.example.microserviceusuarios.dtos.ClienteRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class RestTemplateClientes {

    private static final String RESILIENCE4J_INSTANCE_NAME = "microCircuitBreakerB";
    private static final String FALLBACK_METHOD = "fallback";
    private Integer counterFallback = 0;
    private Integer counter = 0;
    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public List<ClienteRequest> getAllClientes(){
        counter++;
        log.info("Execution N° " + counter + " - Calling micro Clientes");
        ResponseEntity<ClienteRequest[]> response = restTemplate.exchange("http://localhost:8085/cliente/getAllClientes", org.springframework.http.HttpMethod.GET, null, ClienteRequest[].class);
        ClienteRequest[] clientes = response.getBody();
        return Arrays.asList(clientes);
    }
    public List<ClienteRequest> fallback(Exception ex) {
        counterFallback++;
        log.warn("Execution N° {} - FallBack Users - Error message: {}", counter, ex.getMessage());
        return Collections.emptyList();
    }

}
