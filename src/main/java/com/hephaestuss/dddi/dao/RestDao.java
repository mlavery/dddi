package com.hephaestuss.dddi.dao;

import com.hephaestuss.dddi.model.Customer;
import com.hephaestuss.dddi.model.CustomerRest;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RestDao implements CustomerDao {

    @Override
    public Single<Customer> getCustomerById(String customerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();
        params.put("customerId", customerId);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8090/v1/customerByCustomerId/");
        builder.queryParam("customerId", customerId);
        return Observable
                .just(restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(headers), CustomerRest.class).getBody())
                .map(RestDao::convertCustomer)
                .first(new Customer());
    }

    @Override
    public Single<List<Customer>> getAllCustomers() {
        RestTemplate restTemplate = new RestTemplate();
        return Observable
                .fromIterable(restTemplate.getForObject("http://localhost:8090/v1/customers", List.class))
                .map(c -> RestDao.convertCustomer((LinkedHashMap) c))
                .toList();
    }

    @Override
    public Completable saveCustomer(Customer customer) {
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
