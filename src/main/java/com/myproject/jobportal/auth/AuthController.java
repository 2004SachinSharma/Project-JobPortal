package com.myproject.jobportal.auth;

import com.myproject.jobportal.dto.LoginRequestDto;
import com.myproject.jobportal.dto.LoginResponseDto;
import com.myproject.jobportal.dto.RegisterRequestDto;
import com.myproject.jobportal.dto.UserDto;
import com.myproject.jobportal.entity.JobPortalUser;
import com.myproject.jobportal.repository.JobPortalUserRepository;
import com.myproject.jobportal.repository.RoleRepository;
import com.myproject.jobportal.security.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

private final AuthenticationManager authenticationManager;
private final JwtUtil jwtUtil;
private final PasswordEncoder passwordEncoder;
private final RoleRepository roleRepository;
private final JobPortalUserRepository jobPortalUserRepository;

@PostMapping(path = "/login/public", version= "1.0")

public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {

    log.info("Login request received: " + loginRequestDto.toString()); //Just for logging for the values coming from the client, NOT mandatory to put, but I put it for my personal requirement. You may remove it.
   
   try {
       
           Authentication authenticate = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password())
       );
       
       UserDto userDto = new UserDto();
       
       LoginResponseDto loginResponseDto = new LoginResponseDto(
               HttpStatus.OK.getReasonPhrase(),
               userDto,
               jwtUtil.generateJwtToken(authenticate));
       
       return ResponseEntity.ok(loginResponseDto);
       

   } catch (BadCredentialsException e) {
       return buildErrorResponse(HttpStatus.UNAUTHORIZED,
               "Invalid username or password");
   } catch (AuthenticationException e) {
       return buildErrorResponse(HttpStatus.UNAUTHORIZED,
               "Authentication failed");
   } catch (Exception ex) {
       return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
               "An unexpected error occurred");
   }
   
}

@PostMapping(path = "/register/public", version = "1.0")
public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
    JobPortalUser jobportalUser = new JobPortalUser();
    
    // Copies matching fields (name, email, mobileNumber) from DTO to Entity seamlessly.
    // NOTE: 'password' is not copied automatically because field names are intentionally different
    // ('password' in DTO vs 'passwordHash' in Entity) to prevent storing raw credentials.
    BeanUtils.copyProperties(registerRequestDto, jobportalUser);
    
    // Hash the plain text password securely before persisting it to the database
    jobportalUser.setPasswordHash(passwordEncoder.encode(registerRequestDto.password()));
    
    // Assign a default baseline role to the newly registered user
    roleRepository.findById(1L).ifPresent(jobportalUser::setRole);
    
    // Persist the complete user entity to the storage layer
    jobPortalUserRepository.save(jobportalUser);
    
    return ResponseEntity.status(HttpStatus.CREATED)
                   .body("Congratulations! User Registered Successfully");
    
    /*
     * 🏗️ ARCHITECTURE & PRODUCTION SCALABILITY NOTES:
     *
     * 1. Layer Separation:
     *    For simplicity in this initial implementation, the core business logic is kept directly
     *    inside the Controller. As the application grows or for complex enterprise workflows,
     *    this logic should be extracted and delegated to a dedicated Service layer (@Service)
     *    to preserve clean separation of concerns.
     *
     * 2. Missing Business Validation (TODO):
     *    This is a basic registration baseline. In a production environment, we must add a
     *    pre-verification step to check if the user already exists (e.g., checking if the email
     *    is already registered using jobPortalUserRepository.existsByEmail()) before invoking
     *    the save method to prevent duplicate account exceptions.
     *
     * And more improvements still needed...
     *
     */
}



private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status,
                                                            String message) {
    return ResponseEntity
                   .status(status)
                   .body(new LoginResponseDto(message, null, null));
}


}

