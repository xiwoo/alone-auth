package com.inminhouse.alone.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.inminhouse.alone.auth.config.security.LoggingHttpFirewall;
import com.inminhouse.alone.auth.config.security.RestAuthenticationEntryPoint;
import com.inminhouse.alone.auth.config.security.TokenAuthenticationFilter;
import com.inminhouse.alone.auth.config.security.oauth2.HttpCookieAuthorizationRequestRepository;
import com.inminhouse.alone.auth.config.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.inminhouse.alone.auth.config.security.oauth2.OAuth2AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	
	@Autowired
	private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
    	return new TokenAuthenticationFilter();
    }
    
    @Override
    public final void configure(WebSecurity web) throws Exception {
    	super.configure(web);
    	web.httpFirewall(new LoggingHttpFirewall());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	http
    		.cors().and()
    		.csrf().and()
    		.formLogin().disable()
    		.httpBasic().disable()
    		.exceptionHandling()
    			.authenticationEntryPoint(new RestAuthenticationEntryPoint()).and()
    		.authorizeRequests()
//	            .antMatchers("/",
//                    "/error",
//                    "/favicon.ico",
//                    "/**/*.png",
//                    "/**/*.gif",
//                    "/**/*.svg",
//                    "/**/*.jpg",
//                    "/**/*.html",
//                    "/**/*.css",
//                    "/**/*.js"
//                ).permitAll()
    			.antMatchers("/oauth2/**").permitAll()
//    			.anyRequest().authenticated()
    			.and()
    		.oauth2Login()
    			.authorizationEndpoint(authorization ->
    				authorization.baseUri("/oauth2/authorize")
    					.authorizationRequestRepository(new HttpCookieAuthorizationRequestRepository())
    			)
	    		.redirectionEndpoint(redirect -> 
	    			redirect.baseUri("/oauth2/callback/*")
				)
			//successHandler:: ???????????? ?????? ?????? ??? ?????? ????????? ????????? ?????????
			.successHandler(oAuth2AuthenticationSuccessHandler)
			//failureHandler:: ???????????? ?????? ?????? ??? ?????? ????????? ????????? ?????????
			.failureHandler(oAuth2AuthenticationFailureHandler);
    	
    	http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    	
    }
}
