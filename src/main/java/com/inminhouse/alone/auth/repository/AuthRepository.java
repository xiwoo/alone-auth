package com.inminhouse.alone.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inminhouse.alone.auth.config.security.oauth2.user.AuthProvider;
import com.inminhouse.alone.auth.model.Auth;

@Repository
public interface AuthRepository extends JpaRepository<Auth, String> {
	
	Optional<Auth> findByUserIdAndProvider(long userId, AuthProvider provider);
	Optional<Auth> findByProvider(String provider);
	Boolean existsByProvider(String provider);
}
