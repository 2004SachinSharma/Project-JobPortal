package com.myproject.jobportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id", nullable = false)
private Long id;

@Size(max = 50)
@NotNull
@Column(name = "name", nullable = false, length = 50)
private String name;


//@OneToMany(mappedBy = "role")
//private List<JobPortalUser> users; //Generally in real world scenarios we do mention this relationShip both the sides i.e., on the JobPortalUser.java side as well and on Role.java as well...

//But!!
//Whenever we are mentioning these JPA relationship configurations on both entities,
//
//we call such relationships as bidirectional relationships. For our backend application,
//
//I don't want to mention these configurations unnecessarily inside the Roles entity,
//
//because inside our backend application, we are never going to have a scenario to load all the
//
//user details for a given role. Means I will never invoke inside my business logic role.getUsers() to
//
//understand who are all the users assigned to a given role. If we never have such scenario, then
//
//we don't have to maintain the relationship both sides. We can maintain it as unidirectional itself.
//
//That's why I'm going to comment these two lines of code and I'll also mention some
//
//java comments here for your reference on why I have commented this, so OneToMany above on the
//
//role side is optional, because I don't have any scenario to fetch all users by role. That's why
//
//I'm trying to avoid the heavy bi-directional mapping. I hope you are clear.

//Now let's build User Registration REST API...
}