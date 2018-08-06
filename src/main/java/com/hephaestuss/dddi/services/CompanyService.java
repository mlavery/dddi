package com.hephaestuss.dddi.services;

import com.hephaestuss.dddi.dao.CustomerDao;
import com.hephaestuss.dddi.documents.Company;
import com.hephaestuss.dddi.repositories.CompanyRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class CompanyService implements ApplicationContextAware {

    private ApplicationContext appCtx;

    private final CompanyRepository companyRepository;

    public CompanyService(@Autowired CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Mono<Company> getCompany(Long id) {
        return Mono.just(companyRepository.findById(id))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(company -> {
                company.retriever = (CustomerDao) appCtx.getBean(company.getDao());
                    return company;
                }
            );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appCtx = applicationContext;
    }
}
