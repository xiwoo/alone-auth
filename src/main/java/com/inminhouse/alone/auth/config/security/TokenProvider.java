package com.inminhouse.alone.auth.config.security;


import java.util.Date;

import org.springframework.stereotype.Service;

import com.inminhouse.alone.auth.config.AppProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenProvider {

    private AppProperties appProperties;
    
    private static final String CLAIM_KEY = "provider";
    
    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }
    
    public String createToken(UserPrincipal userPrincipal) {
    	
    	Date now = new Date();
    	Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
    	
    	return Jwts.builder()
    			.setSubject(Long.toString(userPrincipal.getId()))
    			.claim(CLAIM_KEY, userPrincipal.getProvider())
    			.setIssuedAt(new Date())
    			.setExpiration(expiryDate)
    			.signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
    			.compact();
    }
    
    public Long getIdFromToken(String token) {
    	
    	try {
    		
    		Claims claims = Jwts.parser()
				.setSigningKey(appProperties.getAuth().getTokenSecret())
				.parseClaimsJws(token)
				.getBody();
    		
    		return Long.parseLong(claims.getSubject());
    	} 
    	catch(Exception e) {
    		log.error("token get id failure ", e);
    		throw e;
    	}
    }
    
    public String getProviderFromToken(String token) {

    	try {
    		
			Claims claims = Jwts.parser()
				.setSigningKey(appProperties.getAuth().getTokenSecret())
				.parseClaimsJws(token)
				.getBody();
			return claims.get(CLAIM_KEY).toString();
    	} 
    	catch(Exception e) {
    		log.error("token get id failure ", e);
    		throw e;
    	}
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
