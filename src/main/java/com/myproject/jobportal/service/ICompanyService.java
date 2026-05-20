package com.myproject.jobportal.service;

import com.myproject.jobportal.dto.CompanyDto;
import com.myproject.jobportal.entity.Company;

import java.util.List;

public interface ICompanyService {

    List<CompanyDto> getAllCompanies();


}
