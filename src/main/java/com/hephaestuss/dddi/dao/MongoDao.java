package com.hephaestuss.dddi.dao;

import com.hephaestuss.dddi.documents.CustomerMongo;
import com.hephaestuss.dddi.model.Customer;
import com.hephaestuss.dddi.repositories.CustomerMongoRepository;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MongoDao implements CustomerDao {

    private final CustomerMongoRepository mongoRepository;

    public MongoDao(@Autowired CustomerMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    public Single<List<Customer>> getCustomerById(String firstName, String lastName) {
        CustomerMongo.MongoCustomerKey id = new CustomerMongo.MongoCustomerKey(firstName, lastName);
        return  Observable
                .just(mongoRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::convertToCustomer)
                .toList();
    }

    @Override
    public Single<Customer> getCustomerById(String customerId) {
        return Completable.complete().<Customer>toObservable().first(new Customer());
    }

    @Override
    public Single<List<Customer>> getAllCustomers() {
        return Observable.fromIterable(mongoRepository.findAll())
                .map(this::convertToCustomer)
                .toList();
    }

    @Override
    public Completable saveCustomer(Customer customer) {
        CustomerMongo customerMongo = customer.getId().isPresent() ?
                new CustomerMongo(customer.getId().get(), customer.firstName, customer.lastName, customer.companyId)
                : new CustomerMongo(customer.firstName, customer.lastName, customer.companyId);
        return Completable.fromAction(() -> mongoRepository.save(customerMongo));
    }

    private Customer convertToCustomer(CustomerMongo customer) {
        return new Customer(null, customer.id.getFirstName(), customer.id.getLastName(), customer.companyId);
    }
}
