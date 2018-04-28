package com.hephaestuss.dddi.services;

import com.hephaestuss.dddi.dao.CustomerDao;
import com.hephaestuss.dddi.documents.Company;
import com.hephaestuss.dddi.repositories.CompanyRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService implements ApplicationContextAware {

    private ApplicationContext appCtx;

    private final CompanyRepository companyRepository;

    public CompanyService(@Autowired CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Optional<Company> getCompany(Long id) {
        Optional<Company> company = companyRepository.findById(id);
        company.ifPresent(
                company1 -> company1.retriever = (CustomerDao) appCtx.getBean(company1.getDao())
        );
        return company;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appCtx = applicationContext;
    }
}
