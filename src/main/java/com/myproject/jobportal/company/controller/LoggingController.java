package com.myproject.jobportal.company.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Demo controller to showcase SLF4J logging levels
 */
@RestController
@RequestMapping("/logging")

@Slf4j
public class LoggingController {

//    private static final Logger logger =
//            LoggerFactory.getLogger(LoggingController.class);

    @GetMapping("/demo")
    public ResponseEntity<String> demoLogs(@RequestParam(defaultValue = "guest") String user) {

        // TRACE → very fine-grained (rarely used in production)
        log.trace("TRACE: Entered demoLogs method for user={}", user);   //All the log levels that are less severe than the configured log level (by default, INFO is provided unless configured in application.properties) will not be recorded or captured.

        // DEBUG → useful for developers during debugging
        log.debug("DEBUG: Processing request for user={}", user);

        // INFO → important business flow information
        log.info("INFO: User={} has hit the logging endpoint", user);

        try {
            // Simulating some processing logic
            if ("error".equalsIgnoreCase(user)) {
                throw new RuntimeException("Simulated exception for testing logs");
            }

            // WARN → something unexpected but not breaking system
            if ("unknown".equalsIgnoreCase(user)) {
                log.warn("WARN: User {} is not recognized", user);
            }

            return ResponseEntity.ok("User processed successfully: " + user);

        } catch (Exception e) {

            // ERROR → actual failure in system
            log.error("ERROR: Exception occurred while processing user={}", user, e);

            return ResponseEntity.status(500)
                    .body("Internal Server Error for user: " + user);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("INFO: Health check endpoint called");
        return ResponseEntity.ok("Service is UP");
    }
}