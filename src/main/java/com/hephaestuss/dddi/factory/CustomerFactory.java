package com.hephaestuss.dddi.factory;

import com.hephaestuss.dddi.dao.CustomerDao;
import com.hephaestuss.dddi.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CustomerFactory {
    private final List<CustomerDao> customerDaoList;

    public CustomerFactory(@Autowired List<CustomerDao> customerDaos) {
        this.customerDaoList = customerDaos;
    }

    public Flux<Mono<Customer>> findById(String id) {
        return Flux.fromIterable(customerDaoList)
                .map(dao -> dao.getCustomerById(id)
                        .filter(Customer::notNull)
                        .map(c -> c.setDao(dao)));
    }
}
