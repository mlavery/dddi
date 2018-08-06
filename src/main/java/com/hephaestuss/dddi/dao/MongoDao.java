package com.hephaestuss.dddi.dao;

import com.hephaestuss.dddi.documents.CustomerMongo;
import com.hephaestuss.dddi.model.Customer;
import com.hephaestuss.dddi.repositories.CustomerMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MongoDao implements CustomerDao {

    private final CustomerMongoRepository mongoRepository;

    public MongoDao(@Autowired CustomerMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    public Mono<Customer> getCustomerById(String firstName, String lastName) {
        CustomerMongo.MongoCustomerKey id = new CustomerMongo.MongoCustomerKey(firstName, lastName);
        return mongoRepository.findById(id)
                .map(this::convertToCustomer);
    }

    @Override
    public Mono<Customer> getCustomerById(String customerId) {
        return Mono.just(new Customer());
    }

    @Override
    public Flux<Customer> getAllCustomers() {
        return mongoRepository.findAll().map(this::convertToCustomer);
    }

    @Override
    public Mono saveCustomer(Customer customer) {
        CustomerMongo customerMongo = customer.getId().isPresent() ?
                new CustomerMongo(customer.getId().get(), customer.firstName, customer.lastName, customer.companyId)
                : new CustomerMongo(customer.firstName, customer.lastName, customer.companyId);
        return mongoRepository.save(customerMongo);
    }

    private Customer convertToCustomer(CustomerMongo customer) {
        return new Customer(null, customer.id.getFirstName(), customer.id.getLastName(), customer.companyId);
    }
}
