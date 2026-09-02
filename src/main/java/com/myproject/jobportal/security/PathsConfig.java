package com.myproject.jobportal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathsConfig {

    public static final String [] PUBLIC_PATHS = {
            /** {@code "/api/companies/public"}
             Moved this public(unsecured) companies path to the SECURED_PATHS list, just to test
                                                       authentication through the in-memory user details in the JobPortalSecurityConfig class.*/
            "/api/contacts/public",
            "/api/swagger-ui.html",
            "/swagger-ui/**",
            "/api/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**"};


    public static final String [] SECURED_PATHS ={
            "/api/**",
            "/api/companies/public"};

}
