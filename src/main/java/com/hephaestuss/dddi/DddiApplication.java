package com.hephaestuss.dddi;

import com.hephaestuss.dddi.dao.CustomerDao;
import com.hephaestuss.dddi.dao.H2Dao;
import com.hephaestuss.dddi.dao.MongoDao;
import com.hephaestuss.dddi.dao.RestDao;
import com.hephaestuss.dddi.documents.Company;
import com.hephaestuss.dddi.documents.CustomerH2;
import com.hephaestuss.dddi.documents.CustomerMongo;
import com.hephaestuss.dddi.factory.CustomerFactory;
import com.hephaestuss.dddi.model.Customer;
import com.hephaestuss.dddi.repositories.CompanyRepository;
import com.hephaestuss.dddi.repositories.CustomerH2Repository;
import com.hephaestuss.dddi.repositories.CustomerMongoRepository;
import com.hephaestuss.dddi.services.CompanyService;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import reactor.core.publisher.BaseSubscriber;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories
@EnableMongoRepositories
public class DddiApplication implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CustomerMongoRepository customerMongoRepository;
    @Autowired
    private CustomerH2Repository customerH2Repository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RestDao restDao;
    @Autowired
    private H2Dao h2Dao;
    @Autowired
    private MongoDao mongoDao;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CustomerFactory customerFactory;


    public static void main(String[] args) {
        SpringApplication.run(DddiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("--- Starting Application ---");
        log.info("Creating tables");

        // Create demo companies
        Company mongoCompany = companyRepository.save(new Company("company1", "mongoDao"));
        Company companySave = companyRepository.save(new Company("company2", "h2Dao"));
        companyRepository.save(new Company("company2", "restDao"));

        populateRandomData(h2Dao, mongoCompany.id);
        populateMongoData(mongoCompany.id);
        populateH2Data(companySave.id);

        mongoCompany = companyService.getCompany(mongoCompany.id).block();
        final Company company = companyService.getCompany(companySave.id).block();

        /*
        Retrieve static customers from Rest endpoint
         */
        restDao.getAllCustomers().buffer(5).subscribe(customers -> {
            log.info("--- Rest Customers ---");
            customers.forEach(c -> log.info("Rest: " + c.toString()));
        },
        throwable -> {
            // Do something complicated
        });

        restDao.getAllCustomers()
            .buffer(5)
            .toStream()
            .flatMap(List::stream)
            .map(Object::toString)
            .forEach(c -> log.info("Rest Stream: " + c));

        /*
        Retrieve Mongo customers
         */
        log.info("--- Mongo Customers ---");
        companyService.getCompany(mongoCompany.id).subscribe(c ->
            company.getAllCustomers().toStream().forEach(customer -> {
                log.info(customer.toString());
                log.info("Find by Name: " +
                mongoDao.getCustomerById("mongo", "read").toString());
            })
            ,throwable -> log.error(throwable.getLocalizedMessage()));

        log.info("--- Mongo Customers With Backpressure---");
        companyService.getCompany(mongoCompany.id).subscribe(c ->
            company.getAllCustomers().buffer(10).subscribe(new BaseSubscriber<List<Customer>>() {
                @Override
                protected void hookOnSubscribe(Subscription subscription) {
                    request(10);
                }
                @Override
                protected void hookOnNext(List<Customer> customers) {
                    customers.forEach(customer -> {
                        log.info(customer.toString());
                        log.info("Find by Name: " + mongoDao.getCustomerById("mongo", "read").toString());
                        request(10);
                    });
                }
            }));

        /*
        Retreieve customer from H2
         */
        log.info("--- H2 Customers ---");
        company.findCustomerById("4")
            .subscribe(customer -> {
                log.info("H2 Customer: " + customer.toString());

                /*
                Save the H2 Customer
                */
                customer.setFirstName("NameChanged");
                customer.saveCustomer().subscribe(object -> {
                        // Do nothing
                    },
                    throwable -> log.error("Error saving customer")
                );
                company.findCustomerById("4")
                    .subscribe( c -> log.info("H2 Customer Name Changed: " + c.toString()),
                    throwable ->  log.error(throwable.getLocalizedMessage())
                );
            },
            throwable -> log.error(throwable.getLocalizedMessage())
        );
        company.getAllCustomers().buffer(5).subscribe(customer -> log.info(customer.toString()));

        /*
        Use CustomerFactory
         */
        log.info("---Customer Factory ---");
        customerFactory.findById("1")
            .subscribe(mono -> mono.subscribe(c ->
                log.info("Customer Factory found user: " + c.toString())
            ),
            throwable -> log.error(throwable.getLocalizedMessage())
        );

        log.info("\n---Finished---");
        System.exit(1);
    }

    private void populateRandomData(CustomerDao dao, Long companyId) {
        for(int i = 0; i < 3; i++) {
            new Customer(
                null,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                companyId)
            .setDao(dao)
            .saveCustomer().block();
        }
    }

    private void populateMongoData(Long companyId) {
        customerMongoRepository.saveAll(Arrays.asList(
                new CustomerMongo("mongo", "read", companyId),
                new CustomerMongo("mongod", "write", companyId),
                new CustomerMongo("mongos", "journal", companyId)
        ));
    }

    private void populateH2Data(Long companyId) {
        customerH2Repository.saveAll(Arrays.asList(
                new CustomerH2("john", "nowhere", "doe", "123 nowhere", companyId),
                new CustomerH2("jane", "somewhere", "doe", "123 nowhere", companyId),
                new CustomerH2("Santa", "", "Clause", "North Pole", companyId),
                new CustomerH2("test", "ing", "123", "", companyId)
        ));
    }
}
