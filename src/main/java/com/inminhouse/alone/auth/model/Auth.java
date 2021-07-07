package com.inminhouse.alone.auth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.inminhouse.alone.auth.config.security.oauth2.user.AuthProvider;
import com.inminhouse.alone.auth.model.audit.CreateAudit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name="auth", uniqueConstraints= {@UniqueConstraint(columnNames="id")})
@ToString
public class Auth extends CreateAudit {
	
	@Id
	private String id;
	
	private long userId;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private AuthProvider provider;

	@Email
	private String email;
	
	@Column(nullable=false)
	private Boolean emailVerified = false;

}
