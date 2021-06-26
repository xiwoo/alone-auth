package com.inminhouse.alone.auth.config.security.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import com.inminhouse.alone.auth.config.security.utils.CookieUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;

@Component
public class HttpCookieAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest>{

	public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int COOKIE_EXPIRE_SECONDS = 180;
	
    //request에 cookie에서 oauth2_auth_request 꺼내어 OAuth2AuthorizationRequest에 담기
	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		
		//oauth2_auth_request 값이 없으면 null 반환
		return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)//Optional 객체 반환
				.map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))//OAuth2AuthorizationRequest 객체에 직렬화 풀어서 담기
				.orElse(null);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
		
		//authorizationRequest가 없으면 상수로 정의된 cookie 삭제
		if(authorizationRequest == null) {
			CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
			CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
			return;
		}
		
		//authorizationRequest 직렬화해서 oauth2_auth_request란 이름으로 cookie에 담기
		CookieUtils.addCookie(
			response, 
			OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, 
			CookieUtils.serialize(authorizationRequest), //authorizationRequest 직렬화
			COOKIE_EXPIRE_SECONDS
		);
		
		//request에서 parameter로 redirect_uri 꺼내기
		String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
		
		//redirect_uri이 있으면 cookie로 담기
		if(StringUtils.isNotBlank(redirectUriAfterLogin)) {
			CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
		}
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		return this.loadAuthorizationRequest(request);
	}
	
	//cookie에서 oauth2_auth_request, redirect_uri 삭제
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }

}
