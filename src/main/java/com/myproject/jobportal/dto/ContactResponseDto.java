package com.myproject.jobportal.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.myproject.jobportal.entity.Contact}
 */
public record ContactResponseDto(
		Long id,
		String name,
		String email,
		String userType,
		String subject,
		String message,
		String status,
		Instant createdAt) implements Serializable {
}