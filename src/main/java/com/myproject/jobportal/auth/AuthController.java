package com.myproject.jobportal.auth;

import com.myproject.jobportal.dto.LoginRequestDto;
import com.myproject.jobportal.dto.LoginResponseDto;
import com.myproject.jobportal.dto.UserDto;
import com.myproject.jobportal.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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


private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status,
                                                            String message) {
    return ResponseEntity
                   .status(status)
                   .body(new LoginResponseDto(message, null, null));
}


}

