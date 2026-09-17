package com.myproject.jobportal.security;

import com.myproject.jobportal.entity.JobPortalUser;
import com.myproject.jobportal.repository.JobPortalUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor

/**  CustomAuthenticationProvider(JobPortalUsernamePwdAuthenticationProvider)
 is created when we need
 custom authentication logic instead of relying only
 on Spring Security's default DaoAuthenticationProvider.
 
 Example: custom database lookup, special validation,
 or additional authentication rules.
 */

public class JobPortalUsernamePwdAuthenticationProvider implements AuthenticationProvider {

private final JobPortalUserRepository jobPortalUserRepository;
private final PasswordEncoder passwordEncoder;

@Override
public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
	String username = authentication.getName();
	
	JobPortalUser user = jobPortalUserRepository.findByEmail(username)
			                     .orElseThrow(() -> new UsernameNotFoundException(
					                     "User details not found for the user" + username
			                     ));
	
	
	List<SimpleGrantedAuthority> authorities = List.of(
			new SimpleGrantedAuthority(user.getRole().getName())
	);
	
	if (passwordEncoder.matches(authentication.getCredentials().toString(), user.getPasswordHash())) {
		return new UsernamePasswordAuthenticationToken(user, null, authorities);
	} else {
		throw new BadCredentialsException("Invalid Password");
	}
}

@Override
public boolean supports(Class<?> authentication) {
	return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	
	// Through this supports() method, our CustomAuthenticationProvider
	// is basically telling Spring Security:
	//
	// "I can only process UsernamePasswordAuthenticationToken".
	//
	// If the incoming authentication token is of type
	// UsernamePasswordAuthenticationToken or any subclass of it,
	// then I can process it.
	//
	// Otherwise, I cannot process it.
	
	// Please check with another AuthenticationProvider,
	// like JwtAuthenticationProvider or another suitable provider.
}
}
/**
 /*
 * LOGIN FLOW
 *
 * React Login Form
 *       ↓
 * AuthController
 *       ↓
 * AuthenticationManager
 *       ↓
 * JobPortalUsernamePwdAuthenticationProvider
 * (Our custom AuthenticationProvider instead of DaoAuthenticationProvider)
 *       ↓
 * authenticate()
 *       ↓
 * Find user by email
 *       ↓
 * BCrypt password verification
 *       ↓
 * Add role authorities
 *       ↓
 * Return authenticated Authentication
 *       ↓
 * JwtUtil generates JWT
 *       ↓
 * AuthController returns JWT response
 *       ↓
 * React receives and stores JWT
 *
 *
 * SUBSEQUENT REQUEST FLOW
 *
 * React sends request with:
 * Authorization: Bearer <JWT>
 *       ↓
 * Spring Security Filter Chain
 *       ↓
 * Custom JWT Validator Filter
 * (OncePerRequestFilter)
 *       ↓
 * Extract Bearer token from Authorization header
 *       ↓
 * Validate JWT signature and expiration
 *       ↓
 * Extract username and roles from JWT claims
 *       ↓
 * Create authenticated Authentication object
 *       ↓
 * Set Authentication in SecurityContextHolder
 *       ↓
 * Continue filter chain
 *       ↓
 * Authorization checks endpoint permissions
 *       ↓
 * Controller executes
 *       ↓
 * Service / Repository logic
 *       ↓
 * Response returned to React
 *
 *
 * IMPORTANT:
 *
 * - AuthenticationProvider authenticates username and password.
 * - JwtUtil generates the JWT after successful login.
 * - Custom JWT Validator Filter validates JWT on subsequent requests.
 * - The JWT Validator Filter directly sets Authentication
 *   in SecurityContextHolder.
 * - It does not call AuthenticationManager or
 *   DaoAuthenticationProvider in this flow.
 * - SecurityContextHolder stores authentication for the current request.
 */

/**
 
 * IMPORTANT:
 *
 * AuthenticationProvider authenticates the user.
 * It does NOT generate the JWT.
 *
 * JwtUtil generates the JWT after successful login.
 *
 * JWT Validator Filter handles subsequent requests
 * by verifying the JWT and setting Authentication
 * in SecurityContextHolder.
 *
 * supports() tells Spring Security:
 * "This provider can process UsernamePasswordAuthenticationToken.
 * Otherwise, check another suitable provider."
 */