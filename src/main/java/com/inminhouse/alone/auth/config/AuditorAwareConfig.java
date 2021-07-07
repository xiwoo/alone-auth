package com.inminhouse.alone.auth.config;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.inminhouse.alone.auth.config.security.UserPrincipal;

import lombok.extern.slf4j.Slf4j;

@EnableJpaAuditing
@Configuration
@Slf4j
public class AuditorAwareConfig implements AuditorAware<Long> {

	@Override
	public Optional<Long> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (null == authentication || !authentication.isAuthenticated()) { 
             return Optional.empty(); 
        }
		
		try {
			UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
			
			if(principal != null) {
				return Optional.of(principal.getId());
			}
			else {
				throw new RuntimeException("not found principal");
			}
			
		} catch(Exception e) {
			log.error("Exception Auditor Aware:: ", e);
			throw e;
		}
	}
}
