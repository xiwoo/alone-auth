package com.inminhouse.alone.auth.config.security.oauth2;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.inminhouse.alone.auth.config.AppProperties;
import com.inminhouse.alone.auth.config.security.TokenProvider;
import com.inminhouse.alone.auth.config.security.UserPrincipal;
import com.inminhouse.alone.auth.config.security.utils.CookieUtils;
import com.inminhouse.alone.auth.exception.BadRequestException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private TokenProvider tokenProvider;
	private AppProperties appProperties;
	private HttpCookieAuthorizationRequestRepository httpCookieAuthorizationRequestRepository;
	
	@Autowired
	OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider, AppProperties appProperties, HttpCookieAuthorizationRequestRepository httpCookieAuthorizationRequestRepository) {
		this.tokenProvider = tokenProvider;
		this.appProperties = appProperties;
		this.httpCookieAuthorizationRequestRepository = httpCookieAuthorizationRequestRepository;
	}
	
	//인증 성공시 처리
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		//SimpleUrlAuthenticationSuccessHandler에 등록된 targetUrlParameter이 있다면 
		//request에서 등록된 targetUrlParameter으로 targetUrl을 꺼낸다
		//만약 언제나 defaultTargetUrl을 쓴다고 설정이 되어있다면 defaultTargetUrl('/')을 반환한다.
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        this.clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
	
	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		
        Optional<String> redirectUri = CookieUtils.getCookie(request, HttpCookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        
        //cookie에 redirectUri가 등록 되어있고, appProperties에 허가하기로 했던 redirectUri가 아니면
        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String token = tokenProvider.createToken(principal);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token).queryParam("status", principal.getStatus().getStatus())
                .build().toUriString();
    }
	
	private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost()) && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
	
	//session과 cookie 정리
	protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
		//request의 기존session에 attribute에서 SPRING_SECURITY_LAST_EXCEPTION 삭제
        super.clearAuthenticationAttributes(request);
        //cookie에서 oauth2_auth_request, redirect_uri 삭제
        httpCookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
