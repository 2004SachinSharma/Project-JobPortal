package com.myproject.jobportal.company.controller;

import com.myproject.jobportal.dto.CompanyDto;
import com.myproject.jobportal.company.service.impl.CompanyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**@CrossOrigin(origins = {"http://localhost:5173"}) can use annotation as well but not recommended, as it specifies the CORS policy for this specific class or method level*/
@RestController
@RequestMapping("/companies")/** Generally we use {/api/...} prefix or in big enterprises (application_name/api/...) prefix just o indicate that this particular class exposes API*/
//But on every class level URI mentioning this prefix does not look good and it becomes overhead adding it to every class level URI, so we can put this prefix at one centralized place from where it would be added automatically

@RequiredArgsConstructor/** {Lombok} annotation to generate constructor */

public class CompanyController {

    private final CompanyServiceImpl companyService;

/**    @Autowired //Since only one constructor; @Autowired is Optional here.
    CompanyController(CompanyServiceImpl companyService) {
        this.companyService = companyService;
    }
No need to make constructor manually generated, instead Lombok annotation will handle this job*/

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
