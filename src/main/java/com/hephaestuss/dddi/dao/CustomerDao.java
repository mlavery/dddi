package com.hephaestuss.dddi.dao;

import com.hephaestuss.dddi.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerDao {

    Mono<Customer> getCustomerById(String customerId);

    Flux<Customer> getAllCustomers();

    Mono saveCustomer(Customer customer);
}
