package ru.gromov.resvote.model;

/*
 *   Created by Gromov Vitaly, 2019   e-mail: mr.gromov.vitaly@gmail.com
 */

import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "users")
public class User extends AbstractNamedEntity {

	@Email
	@NotBlank
	@SafeHtml
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@NotBlank
	@Length(min = 5)
	@SafeHtml
	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
	private boolean enabled = true;

	@Column(name = "registered", columnDefinition = "timestamp default now()")
	private Date registered = new Date();

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	@BatchSize(size = 200)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	private Set<Role> roles;
}
