package com.hephaestuss.dddi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerRest {

    private String id;
    public String firstName;
    public String lastName;

    @JsonIgnore
    public String phoneNumber;

    public CustomerRest() {
        super();
    }

    public CustomerRest(String id, String firstName, String lastName, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
}
