package com.hephaestuss.dddi.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "customers")
public class CustomerMongo {

    @Id
    public MongoCustomerKey id;
    public Long companyId;

    public CustomerMongo(String id, String firstName, String lastName, Long companyId) {
        initialize(firstName, lastName, companyId);
    }

    public CustomerMongo(String firstName, String lastName, Long companyId) {
        initialize(firstName, lastName, companyId);
    }

    public CustomerMongo() {
        super();
    }

    private void initialize(String firstName, String lastName, Long companyId) {
        this.id = new MongoCustomerKey(firstName, lastName);
        this.companyId = companyId;
    }

    public static class MongoCustomerKey implements Serializable {
        private String firstName;
        private String lastName;

        public MongoCustomerKey(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }
}
