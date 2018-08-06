package com.hephaestuss.dddi.documents;

import com.hephaestuss.dddi.dao.CustomerDao;
import com.hephaestuss.dddi.model.Customer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.persistence.*;

@Entity
public class Company implements ApplicationContextAware {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    private String name;

    @Transient
    public CustomerDao retriever;

    private String dao;

    @Transient
    private ApplicationContext appCtx;

    public Company(String name, String dao) {
        this.name = name;
        this.dao = dao;
    }

    public Company(Long id, String name, String dao) {
        this.id = id;
        this.name = name;
        this.dao = dao;
    }

    public Company() {
        super();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appCtx = applicationContext;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(name).append(" ").append(dao).toString();
    }

    public Flux<Customer> getAllCustomers() {
        return retriever.getAllCustomers();
    }

    public Mono<Customer> findCustomerById(String customerId) {
        return retriever.getCustomerById(customerId);
    }

    public String getDao() {
        return dao;
    }
}
