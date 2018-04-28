package com.hephaestuss.dddi.dao;

import com.hephaestuss.dddi.documents.CustomerH2;
import com.hephaestuss.dddi.model.Customer;
import com.hephaestuss.dddi.repositories.CustomerH2Repository;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class H2Dao implements CustomerDao {

    private final CustomerH2Repository repository;

    public H2Dao(@Autowired CustomerH2Repository customerH2Repository) {
        this.repository = customerH2Repository;
    }

    @Override
    public Single<Customer> getCustomerById(String customerId) {
        return Observable
                .just(repository.findById(Long.parseLong(customerId)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::convertCustomer)
                .first(new Customer());
    }

    @Override
    public Single<List<Customer>> getAllCustomers() {
        return Observable
                .fromIterable(repository.findAll())
                .map(this::convertCustomer)
                .toList();
    }

    @Override
    public Completable saveCustomer(Customer customer) {
        CustomerH2 updateCustomer;
        if(customer.getId().isPresent()) {
            Optional<CustomerH2> customerH2 = repository.findById( Long.parseLong(customer.getId().get()));

            if (!customerH2.isPresent()) {
                throw new RuntimeException("Cannot find customer to update");
            }
            updateCustomer = customerH2.get();
            updateCustomer.companyId = customer.companyId;
            updateCustomer.firstName = customer.firstName;
            updateCustomer.lastName = customer.lastName;
        } else {
            updateCustomer = new CustomerH2(customer);
        }
        return Completable.fromAction(() -> repository.save(updateCustomer));

    }

    private Customer convertCustomer(CustomerH2 customer) {
        return new Customer(customer.getId().toString(), customer.firstName, customer.lastName, customer.companyId).setDao(this);
    }
}
