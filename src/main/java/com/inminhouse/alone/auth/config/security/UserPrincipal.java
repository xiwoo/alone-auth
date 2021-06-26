package com.inminhouse.alone.auth.config.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.inminhouse.alone.auth.config.security.oauth2.user.UserStatus;
import com.inminhouse.alone.auth.model.User;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserPrincipal implements OAuth2User, UserDetails {
	
	private Long id;
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private Map<String, Object> attributes;
	private UserStatus status;
	
	public UserPrincipal(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities, UserStatus status) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.status = status;
	}
	
	public static UserPrincipal create(User user, UserStatus status) {
		List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		
		return new UserPrincipal(
			user.getId(),
			user.getEmail(),
			user.getPassword(),
			authorities,
			status
		);
	}
	
	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return String.valueOf(id);
	}

}
