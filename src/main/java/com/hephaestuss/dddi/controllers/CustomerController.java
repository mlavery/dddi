package com.hephaestuss.dddi.controllers;

import com.hephaestuss.dddi.model.Customer;
import com.hephaestuss.dddi.model.CustomerRest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;
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
    @RequestMapping(path = "/v1/customerByCustomerId", method = GET, produces = APPLICATION_STREAM_JSON_VALUE)
    public Mono<CustomerRest> getCustomersById(@Valid String customerId) {
        return Mono.just(customers.get(customerId));
    }

    @ResponseBody
    @RequestMapping(path = "/v1/customers", method = GET, produces = APPLICATION_STREAM_JSON_VALUE)
    public Flux<CustomerRest> getRestCustomers() { return Flux.fromIterable(new ArrayList<>(customers.values()));
    }

    @ResponseBody
    @RequestMapping(path = "/v1/customer", method = PUT)
    public Mono<ResponseEntity> saveRestCustomers(Customer customer) {
        return Mono.just(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

}
