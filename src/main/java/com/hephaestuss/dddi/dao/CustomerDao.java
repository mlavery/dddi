package com.hephaestuss.dddi.dao;

import com.hephaestuss.dddi.model.Customer;
import io.reactivex.Completable;
import io.reactivex.Single;

import java.util.List;

public interface CustomerDao {

    Single<Customer> getCustomerById(String customerId);

    Single<List<Customer>> getAllCustomers();

    Completable saveCustomer(Customer customer);
}
