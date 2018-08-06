package com.hephaestuss.dddi.dao;

import com.hephaestuss.dddi.model.Customer;
import com.hephaestuss.dddi.model.CustomerRest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RestDao implements CustomerDao {

    @Override
    public Mono<Customer> getCustomerById(String customerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();
        params.put("customerId", customerId);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8090/v1/customerByCustomerId/");
        builder.queryParam("customerId", customerId);
        return Mono
                .just(restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(headers), CustomerRest.class).getBody())
                .map(RestDao::convertCustomer);
    }

    @Override
    public Flux<Customer> getAllCustomers() {
        WebClient webClient = WebClient.create("http://localhost:8090");

        return webClient
            .method(HttpMethod.GET)
            .uri("/v1/customers")
            .retrieve()
            .bodyToFlux(Customer.class);
    }

    @Override
    public Mono saveCustomer(Customer customer) {
        throw new RuntimeException("Cannot save users for this Company");
    }

    private static List<Customer> convertRestCustomers(List<LinkedHashMap> customers) {
        return customers
                .stream()
                .map(RestDao::convertCustomer)
                .collect(Collectors.toList());
    }

    private static Customer convertCustomer(LinkedHashMap customer) {
        return new Customer(
                (String) customer.get("id"),
                (String) customer.get("firstName"),
                (String) customer.get("lastName"),
                null);
    }

    private static Customer convertCustomer(CustomerRest customer) {
        return new Customer(null, customer.firstName, customer.lastName, null);
    }
}
