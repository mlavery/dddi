package com.hephaestuss.dddi.documents;

import com.hephaestuss.dddi.model.Customer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CustomerH2 {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public String firstName;
    public String middleName;
    public String lastName;
    public String address;
    public Long companyId;

    public CustomerH2(Long id, String firstName, String middleName, String lastName, String address, Long companyId) {
        this.id = id;
        initialize(firstName, middleName, lastName, address, companyId);
    }

    public CustomerH2(String firstName, String middleName, String lastName, String address, Long companyId) {
        initialize(firstName, middleName, lastName, address, companyId);
    }

    public CustomerH2(Customer customer) {
        this.id = customer.getId().isPresent() ? Long.parseLong(customer.getId().get()) : null;
        initialize(customer.firstName, null, customer.lastName, null, customer.companyId);
    }

    private void initialize(String firstName, String middleName, String lastName, String address, Long companyId) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.address = address;
        this.companyId = companyId;
    }

    public CustomerH2() {
    }

    public Long getId() {
        return id;
    }
}
