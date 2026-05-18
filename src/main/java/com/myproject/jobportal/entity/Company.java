package com.myproject.jobportal.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Date;

@Entity //Indication to Spring DataJPA that this specific class represents a table in the DB
@Table(name="companies")

/** The {@Table} annotation maps a Java entity to a specific database table, allowing you to override the default behavior
 (where Hibernate automatically assumes the table name exactly matches the Class name) to handle custom table names,
 avoid SQL reserved keywords, target specific database schemas, and enforce multi-column unique constraints.*/

public class Company {
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
     private Double rating;

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

     @Column(name = "CREATED_AT", nullable = false)
     private Instant createdAt;

     @Column(name = "CREATED_BY", nullable = false, length = 20)
     private String createdBy;

     @Column(name = "UPDATED_AT" , nullable = true)
     private Instant updatedAt;

     @Column(name = "UPDATED_BY", nullable = true, length = 20)
     private String updatedBy;

}
