package com.myproject.jobportal.auth;

import com.myproject.jobportal.constants.ApplicationConstants;
import com.myproject.jobportal.dto.LoginRequestDto;
import com.myproject.jobportal.dto.LoginResponseDto;
import com.myproject.jobportal.dto.RegisterRequestDto;
import com.myproject.jobportal.dto.UserDto;
import com.myproject.jobportal.entity.JobPortalUser;
import com.myproject.jobportal.entity.Role;
import com.myproject.jobportal.repository.JobPortalUserRepository;
import com.myproject.jobportal.repository.RoleRepository;
import com.myproject.jobportal.security.JobPortalSecurityConfig;
import com.myproject.jobportal.security.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static com.myproject.jobportal.constants.ApplicationConstants.ROLE_JOB_SEEKER;

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
private final CompromisedPasswordChecker compromisedPasswordChecker;
@PostMapping(path = "/login/public", version = "1.0")

//For Login
public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
	
	log.info("Login request received: " + loginRequestDto.toString()); //Just for logging for the values coming from the client, NOT mandatory to put, but I put it for my personal requirement. You may remove it.
	
	try {
		
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password())
		); //At first here the UsernamePasswordAuthenticationToken is sent to Authentication Manager which is implemented as ProviderManager in JobPortalSecurityConfig.java
//		if you see we have returned a bean of it. So from there the ProviderManager bean is registered in to the context and injected here via constructor injection
//		so here .authenticate() method is actually invoked from the ProviderManager bean/object. ProviderManager is responsible to iterate over all the AuthenticationProvider those are provided to it.
//      if you will go to the same JobPortalSecurityConfig you will see that we have passed a bean of AuthenticationProvider which for which we have configured our custom AuthenticationProvider and annotated as
//		@Component, now as JobPortalUsernamePwdAuthenticationProvider implemented the AuthenticationProvider so the bean of AuthenticationProvider will be created, and it will be injected to the ProviderManager
//		Bean constructor via DI, upon bean creation.
//        And then the ProviderManager will verify that whether the passed UsernamePasswordAuthenticationToken throught the .authenticate(...) method defined above, is supported by which AuthenticationProvider
//		with the help of the support() methods of the AuthenticationProviders , like how we have defined below in our custom AuthenticationProvider (JobPortalUsernamePwdAuthenticationProvider). And as soon it finds supported one it simply passes that
//		UsernamePasswordAuthenticationToken	to the supported AuthProvider like our custom one (JobPortalUsernamePwdAuthenticationProvider)
//      Now if all goes fine and both username and password is correct, then JobPortalUsernamePwdAuthenticationProvider will return the authenticated Authentication object (UsernamePasswordAuthenticationToken) back to the AuthController here
//     and is stored into this same variable above  named as 'authenticate'

//And then in the below steps the principle is extracted out, and the same UsernamePasswordAuthenticationToken is passed for generation of JWT, and finally after all is done the Response is sent back with HttpStatus with message, userDetails, and Token
		
		UserDto userDto = new UserDto();
		JobPortalUser loggedInUser = (JobPortalUser) authenticate.getPrincipal();
		
		BeanUtils.copyProperties(loggedInUser, userDto);
		userDto.setRole(loggedInUser.getRole().getName());
		userDto.setUserId(loggedInUser.getId());
		
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

//For Registration
@PostMapping(path = "/register/public", version = "1.0")
public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
	
	CompromisedPasswordDecision compromisedPasswordDecision = compromisedPasswordChecker.check(registerRequestDto.password());
	if(compromisedPasswordDecision.isCompromised()){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				        .body(Map.of("password","Kindly choose strong password"));
	}
	
	Optional<JobPortalUser> jobPortalUser = jobPortalUserRepository.readUserByEmailOrMobileNumber(registerRequestDto.email(), registerRequestDto.mobileNumber());
	
	if (jobPortalUser.isPresent()) {
		Map<String, String> errors = new HashMap<>();
		JobPortalUser user = jobPortalUser.get();
		
		if (user.getEmail().equalsIgnoreCase(registerRequestDto.email())) {
			errors.put("email", "Email already registered");
		}
		
		if (user.getMobileNumber().equalsIgnoreCase(registerRequestDto.mobileNumber())) {
			errors.put("mobileNumber", "Mobile number already registered");
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	JobPortalUser jobportalUser = new JobPortalUser();
	
	// Copies matching fields (name, email, mobileNumber) from DTO to Entity seamlessly.
	// NOTE: 'password' is not copied automatically because field names are intentionally different
	// ('password' in DTO vs 'passwordHash' in Entity) to prevent storing raw credentials.
	BeanUtils.copyProperties(registerRequestDto, jobportalUser);
	
	// Hash the plain text password securely before persisting it to the database
	jobportalUser.setPasswordHash(passwordEncoder.encode(registerRequestDto.password()));
	
	// Assign a default baseline role to the newly registered user
	Role role = roleRepository.findRoleByName(ApplicationConstants.ROLE_JOB_SEEKER)
			            .orElseThrow(() -> new IllegalArgumentException("Role not found :" + ROLE_JOB_SEEKER));
	
	jobportalUser.setRole(role);
	
	// Persist the complete user entity to the storage layer
	jobPortalUserRepository.save(jobportalUser);
	
	return ResponseEntity.status(HttpStatus.CREATED)
			       .body("Congratulations! User Registered Successfully");
	
}


private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status,
                                                            String message) {
	return ResponseEntity
			       .status(status)
			       .body(new LoginResponseDto(message, null, null));
}


}

