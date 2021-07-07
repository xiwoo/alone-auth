package com.inminhouse.alone.auth.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.inminhouse.alone.auth.services.CustomUserDetailsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		try {
			
	        String jwt = getJwtFromRequest(request);

	        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

	        	Long id = tokenProvider.getIdFromToken(jwt);
	        	String provider = tokenProvider.getProviderFromToken(jwt);
	        	
	        	log.debug("token get id:: {}", id);
	            UserDetails userDetails = customUserDetailsService.loadUserById(id, provider);
	            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	        }
	    } catch (Exception ex) {
	    	log.error("Could not set user authentication in security context", ex);
	    }
	
	    filterChain.doFilter(request, response);
	}
	
	//request에서 jwt 토큰 값 꺼내서 return
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
