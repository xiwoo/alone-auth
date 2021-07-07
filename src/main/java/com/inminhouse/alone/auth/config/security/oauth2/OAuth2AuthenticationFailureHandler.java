package com.inminhouse.alone.auth.config.security.oauth2;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.inminhouse.alone.auth.config.security.utils.CookieUtils;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private static final String QUERYPARAM_ERROR = "error";
	
    @Autowired
    HttpCookieAuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    	
//    	exception.printStackTrace();
    	
    	logger.error("Authorization failure!!");
    	logger.error("EXCEPTION:: ", exception);
    	
        String targetUrl = CookieUtils.getCookie(request, HttpCookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
            .map(Cookie::getValue)
            .orElse(("/"));
        
        //Exception에 따른 error 메세지 처리 필요

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam(QUERYPARAM_ERROR, exception.getLocalizedMessage())
            .build().toUriString();

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
