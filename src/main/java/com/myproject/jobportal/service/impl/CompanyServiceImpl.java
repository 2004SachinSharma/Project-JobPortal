package com.myproject.jobportal.service.impl;

import com.myproject.jobportal.entity.Company;
import com.myproject.jobportal.repository.CompanyRepository;
import com.myproject.jobportal.service.ICompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements ICompanyService {

   private final CompanyRepository companyRepository;


//    @Autowired
//    public CompanyServiceImpl(CompanyRepository companyRepository) {
//        this.companyRepository = companyRepository;
//    }

    @Override
    public List<Company> getAllCompanies(){

        List<Company> companyList = companyRepository.findAll();
        return companyList;

    }



}
