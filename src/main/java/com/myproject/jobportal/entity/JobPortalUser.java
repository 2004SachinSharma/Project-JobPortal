package com.myproject.jobportal.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "users")
@jakarta.persistence.AttributeOverrides({
@jakarta.persistence.AttributeOverride(name = "createdAt",
column = @jakarta.persistence.Column(nullable = false)),
@jakarta.persistence.AttributeOverride(name = "createdBy",
column = @jakarta.persistence.Column(nullable = false,
length = 20)),
@jakarta.persistence.AttributeOverride(name = "updatedAt",
column = @jakarta.persistence.Column),
@jakarta.persistence.AttributeOverride(name = "updatedBy",
column = @jakarta.persistence.Column(length = 20))})
public class JobPortalUser extends com.myproject.jobportal.entity.BaseEntity {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.validation.constraints.Size(max = 255)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "name", nullable = false)
private java.lang.String name;

@jakarta.validation.constraints.Size(max = 255)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "email", nullable = false)
private java.lang.String email;

@jakarta.validation.constraints.Size(max = 500)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "password_hash", nullable = false, length = 500)
private java.lang.String passwordHash;

@jakarta.validation.constraints.Size(max = 20)
@jakarta.persistence.Column(name = "mobile_number", length = 20)
private java.lang.String mobileNumber;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "role_id", nullable = false)
private com.myproject.jobportal.entity.Role role;

@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
@org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.SET_NULL)
@jakarta.persistence.JoinColumn(name = "company_id")
private com.myproject.jobportal.entity.Company company;



}