package com.myproject.jobportal.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.myproject.jobportal.entity.Contact}
 */
public record ContactRequestDto(String email,
                                String message,
                                String name,
                                String subject,
                                String userType
) implements Serializable
{
}