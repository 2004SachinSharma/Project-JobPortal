package com.myproject.jobportal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathsConfig {

    public static final String [] PUBLIC_PATHS = {"/api/companies/public",
            "/api/contacts/public",
            "/api/swagger-ui.html",
            "/swagger-ui/**",
            "/api/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**"};


    public static final String [] SECURED_PATHS ={"/api/**"};

}
