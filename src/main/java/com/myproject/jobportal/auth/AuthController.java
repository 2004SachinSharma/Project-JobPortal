package com.myproject.jobportal.auth;

import com.myproject.jobportal.dto.LoginRequestDto;
import com.myproject.jobportal.dto.LoginResponseDto;
import com.myproject.jobportal.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

@PostMapping("/login/public")

public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
    

    //Note: we are not performing any password and username verification. Despite password is anything, the response will be returned is only the LoginResponse

    UserDto userDto= new UserDto();
    
    //Note: we are not performing any password and username verification.Means despite password is provided anything from the client, the response will be returned is only the LoginResponseDto with the
    // message,
    // user,
    // jwtToken.
    // where for now we are just returning the empty user object...
    // but if you want then for mocking some data, you can easily comment-off the below comments for a builder pattern to set some data.


//    UserDto userDto= UserDto
//                              .builder()
//                              .userId(1L)
//                              .email("sachin@1212gaim.com")
//                             .role("Java AI Engineer")
//                             .name("Sachin Sharma")
//                             .companyName("Google")
//                             .companyId(212L)
//                             .createdAt(Instant.now())
//                             .mobileNumber("74897563342")
//                             .build();

//Ensure putting @Builder Lombok annotation first on UserDto for above purpose.

log.info("Login request received: "+ loginRequestDto.toString()); //Just for logging for the values coming from the client, NOT mandatory to put, but I put it for my personal requirement. You may remove it.

//    log.info(userDto);
    
    LoginResponseDto loginResponseDto = new LoginResponseDto(
            HttpStatus.OK.getReasonPhrase(),
            userDto,
            null);
    
    return ResponseEntity.ok(loginResponseDto);


}


}
