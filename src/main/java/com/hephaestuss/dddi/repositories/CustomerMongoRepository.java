package com.hephaestuss.dddi.repositories;

import com.hephaestuss.dddi.documents.CustomerMongo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerMongoRepository extends ReactiveCrudRepository<CustomerMongo, CustomerMongo.MongoCustomerKey> {
}
