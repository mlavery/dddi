package com.hephaestuss.dddi.controllers;

import com.hephaestuss.dddi.model.Customer;
import com.hephaestuss.dddi.model.CustomerRest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
class CustomerController {

    private final static Map<String, CustomerRest> customers = new HashMap<>();
    static {
        customers.put("1" ,new CustomerRest("1", "aFirst", "aLast", "1-555-5555"));
        customers.put("2" , new CustomerRest("2", "bFirst", "bLast", "+44 1632 960983"));
    }

    @ResponseBody
    @RequestMapping(path = "/v1/customerByCustomerId", method = GET)
    public CustomerRest getCustomersById(@Valid String customerId) {
        return customers.get(customerId);
    }

    @ResponseBody
    @RequestMapping(path = "/v1/customers", method = GET)
    public List<CustomerRest> getRestCustomers() {
        return new ArrayList<>(customers.values());
    }

    @ResponseBody
    @RequestMapping(path = "/v1/customer", method = PUT)
    public ResponseEntity saveRestCustomers(Customer customer) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
