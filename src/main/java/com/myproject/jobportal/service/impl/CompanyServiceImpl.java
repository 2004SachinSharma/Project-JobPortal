package com.myproject.jobportal.service.impl;

import com.myproject.jobportal.dto.CompanyDto;
import com.myproject.jobportal.entity.Company;
import com.myproject.jobportal.repository.CompanyRepository;
import com.myproject.jobportal.service.ICompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;


//    @Autowired
//    public CompanyServiceImpl(CompanyRepository companyRepository) {
//        this.companyRepository = companyRepository;
//    }

    @Override
    public List<CompanyDto> getAllCompanies() {

        List<Company> companyList = companyRepository.findAll();
        // companyList: Database se aayi hui Company objects ki list.

        return companyList.stream().map(this::transformToDto).collect(Collectors.toList());
        // .stream(): List ko process karne ke liye ek sequence (dhara) mein badalta hai.
        // .map(this::transformToDto): Har ek 'Company' object ke liye 'transformToDto' method ko call karta hai.
        //                            (Agar list mein 10 companies hain, toh ye 10 baar call hoga aur 10 CompanyDto return karega).
        // .collect(Collectors.toList()): Saare returned CompanyDto objects ko ik इकट्ठा karke ek nayi List banata hai.
        //aur firr wo list return ho jaati h 'return' keyword se.
    }

    private CompanyDto transformToDto(Company company) {
        return new CompanyDto(
                company.getId(),
                company.getName(),
                company.getLogo(),
                company.getIndustry(),
                company.getSize(),
                company.getRating(),
                company.getLocations(),
                company.getFounded(),
                company.getDescription(),
                company.getEmployees(),
                company.getWebsite(),
                company.getCreatedAt()
        );
    }



}
