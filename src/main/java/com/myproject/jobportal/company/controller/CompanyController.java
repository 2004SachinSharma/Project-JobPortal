package com.myproject.jobportal.company.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies") /** Generally we use {/api/...} prefix or in big enterprises (application_name/api/...) prefix just o indicate that this particular class exposes API*/
//But on every class level URI mentioning this prefix does not look good and it becomes overhead adding it to every class level URI, so we can put this prefix at one centralized place from where it would be added autimatically
public class CompanyController {

    @GetMapping(version="1.0")
    public ResponseEntity<String> getAllCompanies() {
       return ResponseEntity.ok("All Companies");
   }

    @GetMapping(version="2.0")
    public ResponseEntity<String> getAllCompanies2() {
        return ResponseEntity.ok("All Companies2");
    }

}
