package com.hephaestuss.dddi.model;

import com.hephaestuss.dddi.dao.CustomerDao;
import io.reactivex.Completable;

import java.util.Optional;

public class Customer {
    private Optional<String> id;
    public String firstName;
    public String lastName;
    public Long companyId;
    private CustomerDao customerDao;


    public Customer(String id, String firstName, String lastName, Long companyId) {
        this.id = Optional.ofNullable(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyId = companyId;
    }

    public Customer() {
    }

    @Override
    public String toString() {
        return new StringBuilder().append(firstName).append(" ").append(lastName).toString();
    }

    public Optional<String> getId() {
        return id;
    }

    public Customer setId(String id) {
        this.id = Optional.ofNullable(id);
        return this;
    }

    public Customer setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Customer setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Customer setCompanyId(Long companyId) {
        this.companyId = companyId;
        return this;
    }

    public Customer setDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
        return this;
    }

    public Completable saveCustomer() {
        return customerDao.saveCustomer(this);
    }

    public static boolean notNull(Customer customer) {
        return (null != customer && null != customer.firstName && null != customer.lastName);
    }
}
