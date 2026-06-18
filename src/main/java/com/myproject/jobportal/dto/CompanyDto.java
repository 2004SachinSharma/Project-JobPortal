package com.myproject.jobportal.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Data Transfer Object (DTO) for {@link com.myproject.jobportal.entity.Company}.
 *
 * <p><strong>The DTO Pattern:</strong></p>
 * This pattern is used to transfer data between processes or layers of an application.
 * By using DTOs, we decouple the internal database schema (Entities) from the external API representation.
 * This provides several benefits:
 * <ul>
 *     <li><strong>Security:</strong> Prevents over-exposure of sensitive entity fields (e.g., internal IDs, audit fields).</li>
 *     <li><strong>Performance:</strong> Reduces the amount of data transferred by including only necessary fields.</li>
 *     <li><strong>Network Efficiency:</strong> Minimizes network round trips; multiple related records can be grouped and returned in a single response.</li>
 *     <li><strong>API Stability:</strong> Changes to the database schema don't necessarily break the public API.</li>
 * </ul>
 *
 * <p><strong>Why Java Records?</strong></p>
 * Introduced in Java 14/16, Records are ideal for DTOs because they are:
 * <ul>
 *     <li><strong>Immutable by design:</strong> Ensures data integrity during transfer.</li>
 *     <li><strong>Concise:</strong> Automatically generates boilerplate code (constructors, getters, equals, hashCode, and toString).</li>
 *     <li><strong>Clear Intent:</strong> Explicitly signals that the class is a transparent carrier for immutable data.</li>
 *     <li><strong>Pure Data Carrier:</strong> Designed purely to store values or data; they should not contain any business logic.</li>
 * </ul>
 *
 * @param id Unique identifier of the company
 * @param name Official name of the company
 * @param logo URL or path to the company's logo
 * @param industry The business sector the company operates in
 * @param size Company size category (e.g., Small, Medium, Large)
 * @param rating Average user or industry rating
 * @param locations Geographic locations where the company has presence
 * @param founded The year the company was established
 * @param description Detailed overview of the company's mission and business
 * @param employees Total number of employees
 * @param website Official URL of the company
 * @param createdAt Timestamp when the record was created
 */
public record CompanyDto(
        Long id,
        String name,
        String logo,
        String industry,
        String size,
        BigDecimal rating,
        String locations,
        Integer founded,
        String description,
        Integer employees,
        String website,
        Instant createdAt,
        List<JobDto> jobs
) {
}
