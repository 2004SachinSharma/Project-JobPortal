package com.myproject.jobportal.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component

public class AuditAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        // Abhi ke liye hardcoded "System" return kar rahe hain
        // Baad mein yahan Spring Security se user ka naam nikalenge
        return Optional.of("System");
    }
}