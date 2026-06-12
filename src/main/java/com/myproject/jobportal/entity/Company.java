package com.myproject.jobportal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Entity //Indication to Spring DataJPA that this specific class represents a table in the DB
@Table(name="companies")

/** The {@Table} annotation maps a Java entity to a specific database table, allowing you to override the default behavior
 (where Hibernate automatically assumes the table name exactly matches the Class name) to handle custom table names,
 avoid SQL reserved keywords, target specific database schemas, and enforce multi-column unique constraints.*/
@Getter //Lombok annotations to generate getters
@Setter //Lombok annotations to generate setters
public class Company extends BaseEntity {
     @Column(name = "ID", nullable = false) /** same behaviour as {@Table} but for Columns*/
     @Id
     @GeneratedValue(strategy=GenerationType.IDENTITY) //we have multiple strategies to generate the sequence
     private Long id;

     @Column(name = "NAME", nullable = false, unique = true,  length = 255)
     private String name;

     @Column(name = "LOGO", length = 500)
     private String logo;

     @Column(name = "INDUSTRY", nullable = false,  length = 100)
     private String industry;

     @Column(name = "SIZE", nullable = false, length = 50)
     private String size;

     @Column(name = "RATING", nullable = false, precision = 3, scale = 2)
     private BigDecimal rating;

     @Column(name ="LOCATIONS", nullable = false,  length = 1000)
     private String locations;

     @Column(name = "FOUNDED", nullable = false)
     private Integer founded;

     @Lob
     @Column(name = "DESCRIPTION")
     private String description;

     @Column(name = "EMPLOYEES")
     Integer employees;

     @Column(name = "WEBSITE", length = 500)
     private String website;

     //Shifted Created_at and Created_by Columns to the BaseEntityClass in order to leverage JPA Auditing

     //Similarly, Shifted Updated_at and Updted_by Columns to the BaseEntityClass in order to leverage JPA Auditing


/**
     public Long getId() {      //Do not worry, they are needed, but Lombok will handle creating them, no manual creation
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public String getLogo() {
          return logo;
     }

     public void setLogo(String logo) {
          this.logo = logo;
     }

     public String getIndustry() {
          return industry;
     }

     public void setIndustry(String industry) {
          this.industry = industry;
     }

     public String getSize() {
          return size;
     }

     public void setSize(String size) {
          this.size = size;
     }

     public BigDecimal getRating() {
          return rating;
     }

     public void setRating(BigDecimal rating) {
          this.rating = rating;
     }

     public String getLocations() {
          return locations;
     }

     public void setLocations(String locations) {
          this.locations = locations;
     }

     public Integer getFounded() {
          return founded;
     }

     public void setFounded(Integer founded) {
          this.founded = founded;
     }

     public String getDescription() {
          return description;
     }

     public void setDescription(String description) {
          this.description = description;
     }

     public Integer getEmployees() {
          return employees;
     }

     public void setEmployees(Integer employees) {
          this.employees = employees;
     }

     public String getWebsite() {
          return website;
     }

     public void setWebsite(String website) {
          this.website = website;
     }

     public Instant getCreatedAt() {
          return createdAt;
     }

     public void setCreatedAt(Instant createdAt) {
          this.createdAt = createdAt;
     }

     public String getCreatedBy() {
          return createdBy;
     }

     public void setCreatedBy(String createdBy) {
          this.createdBy = createdBy;
     }

     public Instant getUpdatedAt() {
          return updatedAt;
     }

     public void setUpdatedAt(Instant updatedAt) {
          this.updatedAt = updatedAt;
     }

     public String getUpdatedBy() {
          return updatedBy;
     }

     public void setUpdatedBy(String updatedBy) {
          this.updatedBy = updatedBy;
     }
*/

}
