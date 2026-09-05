package com.myproject.jobportal.constants;

public class ApplicationConstants { // This is an utility class created, as needed for defining some constants the application needs!
  
    private ApplicationConstants() {
        throw new AssertionError("Utility class cannnot be instantiated.");
    }//To do not let anyone outside of the class to create the object of this ApplicationConstants class.

     //we should always block object creation for utility classes.
     //As here constructor is private so object cannot be created inside any other class but in this same class it can
     //still be still be created, and compiler will allow that.
     //But at runtime it would throw error that we have put inside it 'see above', and it would technically fail in the same class as well.


// Name of the configuration/environment property that will contain the JWT secret.
// The actual JWT secret value will be fetched later using Spring's @Value annotation.
public static final String JWT_SECRET_KEY = "JWT_SECRET";

// Default JWT secret value used only for development/testing as a fallback value, when no JWT_SECRET configuration is provided.
// In a production application, the secret should be provided through an environment variable
// or an external secret management system instead of being hardcoded.
public static final String JWT_SECRET_DEFAULT_VALUE = "jxgEQeXhuPq8VdbyYFNkANdudQ53YUn4";

/**
 * Look for JWT_SECRET
 *        ↓
 * Environment variable exists?
 *        ↓
 * YES → use its value
 * NO  → use default value*/


//Important: In a real production application, avoid hardcoding the actual JWT secret in source code.
// Use an environment variable or external secret management system.

}
