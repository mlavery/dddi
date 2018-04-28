package com.hephaestuss.dddi.factory;

import com.hephaestuss.dddi.dao.CustomerDao;
import com.hephaestuss.dddi.model.Customer;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerFactory {
    private final List<CustomerDao> customerDaoList;

    public CustomerFactory(@Autowired List<CustomerDao> customerDaos) {
        this.customerDaoList = customerDaos;
    }

    public Single<List<Maybe<Customer>>> findById(String id) {
        return Observable.fromIterable(customerDaoList)
                .map(dao -> dao.getCustomerById(id)
                        .filter(Customer::notNull)
                        .map(c -> c.setDao(dao)))
                .toList();
    }
}
