package com.myproject.jobportal.security.filter;

import com.myproject.jobportal.constants.ApplicationConstants;
import com.myproject.jobportal.security.PathsConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtTokenValidatorFilter extends OncePerRequestFilter {//in this class basic Highlevel flow is,
// Extraction of JWT Token ->
// verify its validity ->
// if valid then create authentication object with principle, roles... etc and save into security context.

private AntPathMatcher antPathMatcher = new AntPathMatcher();


@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader(ApplicationConstants.JWT_HEADER); //This refers to the Authorization header defined in the ApplicationConstants class
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        try {
//            Extract the JWT token
//            Whoever bears or holds the token is trusted and can access the protected resources
            String jwt = authHeader.substring(7); //Remove Bearer prefix
             Environment env = getEnvironment();
            
            if (null != env) {
                String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                        ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                
                if (null != secretKey) {
                    
                    Claims claims = Jwts.parser().verifyWith(secretKey)
                                            .build()
                                            .parseSignedClaims(jwt)
                                            .getPayload();
                    
                    String username = String.valueOf(claims.get("username"));
                    String roles = String.valueOf(claims.get("roles"));
                    
                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                            AuthorityUtils.commaSeparatedStringToAuthorityList(roles));
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token Expired");
            return;
        } catch (Exception e) {
            
            throw new BadCredentialsException("Invalid Token Received");
        }
        
    }
    filterChain.doFilter(request, response);
    
}

@Override
protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return Arrays.stream(PathsConfig.PUBLIC_PATHS).anyMatch(publicPath -> antPathMatcher.match(publicPath, path));
  }
}
