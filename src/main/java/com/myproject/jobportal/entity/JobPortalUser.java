package com.myproject.jobportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Listen, this Entity class and previous Role Entity class as well, generated through the Reverse Engineering plugin
 * <img src="https://plugins.jetbrains.com/files/23738/1167827/icon/default.svg">
 * {@link <a href="https://plugins.jetbrains.com/plugin/23738-reverse-engineering"></a>}
 */

@Getter
@Setter
@Entity
@Table(name = "users")

/*
 * //Ok you may think, Why to remove the AttributesOverride annotation below, when even the plugin provides it...
 *
 * =========================================================================
 * 1. WHY IS IT THERE IN THE FIRST PLACE?
 * =========================================================================
 * - It is used to CHANGE or RENAME the columns we get from the parent class (BaseEntity).
 * - The IDE wizard generates it AUTOMATICALLY just to be safe, because it doesn't
 *   know if you want to change the parent settings or not.
 *
 * =========================================================================
 * 2. WHAT HAPPENS IF IT IS NOT THERE? (If we delete it)
 * =========================================================================
 * - If you DELETE it: The child class will automatically copy everything exactly
 *   as written in the 'BaseEntity' class. The code runs perfectly and looks very clean.
 *
 * - If you NEED it: You only keep it if a specific table needs a different rule.
 *   For example: If BaseEntity says length=20, but the 'users' table strictly
 *   needs length=100 in the real database. Without it here, the system will crash
 *   due to a size mismatch.
 *
 * =========================================================================
 * 3. SUMMARY: WHY DOES THE INSTRUCTOR DELETE IT?
 * =========================================================================
 * - Because our 'BaseEntity' configuration already matches our database script perfectly.
 * - Writing the same column settings twice is a waste of code (Code Bloat).
 * - If we change a column in 'BaseEntity' tomorrow, it will update the entire
 *   project at once. If we keep overrides, we have to fix every file manually.
 *
 * 💡 GOLDEN RULE: Let the IDE create it -> Check it -> Delete it if it matches
 *    the parent. Keep it ONLY if you need a unique change for that specific table.
 *    But here we already have all four columns already matching, so why not to remove it, if unnecessarily there!
 */

/*
@AttributeOverrides({
		@AttributeOverride(name = "createdAt",
				column = @Column(nullable = false)),
		@AttributeOverride(name = "createdBy",
				column = @Column(nullable = false,
						length = 20)),
		@AttributeOverride(name = "updatedAt",
				column = @Column),
		@AttributeOverride(name = "updatedBy",
				column = @Column(length = 20))})*/


public class JobPortalUser extends BaseEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id", nullable = false)
private Long id;

@Size(max = 255)
@NotNull
@Column(name = "name", nullable = false)
private String name;

@Size(max = 255)
@NotNull
@Column(name = "email", nullable = false)
private String email;

@Size(max = 500)
@NotNull
@Column(name = "password_hash", nullable = false, length = 500)
private String passwordHash;

@Size(max = 20)
@Column(name = "mobile_number", length = 20)
private String mobileNumber;

@NotNull
@ManyToOne(fetch = FetchType.EAGER, optional = false)
@JoinColumn(name = "role_id", nullable = false)
private Role role;

@ManyToOne(fetch = FetchType.EAGER)
@OnDelete(action = OnDeleteAction.SET_NULL)
@JoinColumn(name = "company_id")
private Company company;


}