package com.myproject.jobportal.company.controller;

import com.myproject.jobportal.dto.CompanyDto;
import com.myproject.jobportal.entity.Company;
import com.myproject.jobportal.service.impl.CompanyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/companies") /** Generally we use {/api/...} prefix or in big enterprises (application_name/api/...) prefix just o indicate that this particular class exposes API*/
//But on every class level URI mentioning this prefix does not look good and it becomes overhead adding it to every class level URI, so we can put this prefix at one centralized place from where it would be added autimatically

@RequiredArgsConstructor //Lombok annotation to generate constructor
public class CompanyController {

    private final CompanyServiceImpl companyService;

//    @Autowired //Since only one constructor; @Autowired is Optional here.
//    CompanyController(CompanyServiceImpl companyService) {
//        this.companyService = companyService;
//    }
//No need to make constructor manually generated, instead Lombok annotation will handle this job

    @GetMapping(version="1.0")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        List<CompanyDto> companyList = companyService.getAllCompanies();
       return ResponseEntity.ok(companyList);
   }

    @GetMapping(version="2.0")
    public ResponseEntity<String> getAllCompanies2() {
        return ResponseEntity.ok("All Companies2");
    }
}
