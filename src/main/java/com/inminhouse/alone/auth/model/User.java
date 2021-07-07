package com.inminhouse.alone.auth.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.inminhouse.alone.auth.model.audit.FullAudit;
import com.inminhouse.alone.auth.model.enumeration.Gender;
import com.inminhouse.alone.auth.model.enumeration.GenderConverter;
import com.inminhouse.alone.auth.model.enumeration.Level;
import com.inminhouse.alone.auth.model.enumeration.LevelConverter;
import com.inminhouse.alone.auth.model.enumeration.UserStatus;
import com.inminhouse.alone.auth.model.enumeration.UserStatusConverter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name="user", uniqueConstraints= {@UniqueConstraint(columnNames="id")})
@ToString
public class User extends FullAudit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private String name;
	
	private String imageUrl;
	
	@JsonIgnore
	private String password;
	
	private String introduce;
	
	private String birthday;

	@Convert(converter = GenderConverter.class)
	private Gender gender;

	@NotNull
	@Convert(converter = LevelConverter.class)
	private Level level;
	
	@NotNull
	@Convert(converter = UserStatusConverter.class)
	private UserStatus status;
	
}
