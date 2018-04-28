package com.hephaestuss.dddi.repositories;

import com.hephaestuss.dddi.documents.CustomerMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerMongoRepository extends MongoRepository<CustomerMongo, CustomerMongo.MongoCustomerKey> {
}
