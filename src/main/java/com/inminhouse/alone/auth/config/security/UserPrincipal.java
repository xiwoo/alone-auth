package com.inminhouse.alone.auth.config.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.inminhouse.alone.auth.config.security.oauth2.user.UserLoginStatus;
import com.inminhouse.alone.auth.model.Auth;
import com.inminhouse.alone.auth.model.User;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserPrincipal implements OAuth2User, UserDetails {
	
	private Long id;
	private String authId;
	private String provider;
	private String name;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private Map<String, Object> attributes;
	private UserLoginStatus status;
	
	public UserPrincipal(Long id, String authId, String provider, String name, String password, Collection<? extends GrantedAuthority> authorities, UserLoginStatus status) {
		this.id = id;
		this.authId = authId;
		this.provider = provider;
		this.name = name;
		this.password = password;
		this.authorities = authorities;
		this.status = status;
	}
	
	public static UserPrincipal create(User user, Auth auth, UserLoginStatus status) {
		List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		
		return new UserPrincipal(
			user.getId(),
			auth.getId(),
			auth.getProvider().toString(),
			user.getName(),
			user.getPassword(),
			authorities,
			status
		);
	}
	
	@Override
	public String getUsername() {
		return authId;
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

}
