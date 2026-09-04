package com.myproject.jobportal.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Setter
@Getter //Ensure providing Getters, while returning an object in the response, as it is necessarily required by the Jackson to get the values to make the JSON response, if not provided the object will be returned empty i.e. {}

 public class UserDto{
 private Long userId;
 private String name;
 private String email;
 private String mobileNumber;
 private String role;
 private Long companyId;
 private String companyName;
 private Instant createdAt;

}
