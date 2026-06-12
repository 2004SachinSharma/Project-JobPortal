package com.myproject.jobportal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@ToString //Generates a toString() method for this entity, which includes the key = value pair, i.e., "Contact(id=7, createdAt=2026-05-27T16:..."
@Entity
@Table(name = "contact")
public class Contact extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private  Long id;

    //Shifted Created_at and Created_by Columns to the BaseEntityClass in order to leverage JPA Auditing

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @ColumnDefault("'NEW'")
    @Column(name = "status", nullable = false, length = 20)
    private String status = "NEW";

    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    //Similarly, Shifted Updated_at and Updted_by Columns to the BaseEntityClass in order to leverage JPA Auditing

    @Column(name = "user_type", nullable = false, length = 50)
    private String userType;
}