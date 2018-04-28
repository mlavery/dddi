package com.hephaestuss.dddi.repositories;

import com.hephaestuss.dddi.documents.Company;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Long> {
}
