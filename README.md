# ALONE-AUTH

## Please ask for "application-*.yml" before using.

아직 ms 아키텍처 적용 전

구조 및 전체 플로우 참고 출처::

[https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-1/](https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-1/)

## src/main/resources

### application-app.yml

```yaml
app:
	auth:
		token-secret: (jwt token secret code)
		token-expiration-msec: (jwt token set time)
	oauth2:
		# After successfully authenticating with the OAuth2 Provider,
		# we'll be generating an auth token for the user and sending the token to the
		# redirectUri mentioned by the frontend client in the /oauth2/authorize request.
		# We're not using cookies because they won't work well in mobile clients.
		authorized-redirect-uris:
		  - (모든 인증/인가 후 front쪽으로 redirect할 주소)
```

### application-oauth2.yml

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
					#registrationId
          google:
            client-id: (google client id)
            client-secret: (google client secret code)
            redirect-uri: "{baseUrl}(callback uri)/{registrationId}"
            scope:
              - email
              - profile
          facebook:
            client-id: (facebook client id)
            client-secret: (facebook client secret code)
            redirect-uri: "{baseUrl}(callback uri)/{registrationId}"
          naver:
            client-id: (naver client id)
            client-secret: (naver client secret code)
            redirect-uri: "{baseUrl}(callback uri)/{registrationId}"
            authorization-grant-type: authorization_code
            client-authentication-method: POST
            client-name: naver
            scope: 
              - name
              - email
              - profile_image
          kakao:
            client-id: (kakao client id)
            client-secret: (kakao client secret code)
            redirect-uri: "{baseUrl}(callback uri)/{registrationId}"
            authorization-grant-type: authorization_code
            client-authentication-method: POST
            client-name: kakao

				#global하지 않아서인지 naver, kakao는 provider 설정 필요
        provider:
          naver:
            authorization-uri: https://nid.naver.com/oauth2.0/authorize
            token-uri: https://nid.naver.com/oauth2.0/token
            user-info-uri: https://openapi.naver.com/v1/nid/me
            user-name-attribute: (발급된 token으로 userinfo 가져왔을 때 확인 할 key)
          kakao:
            authorization-uri: https://kauth.kakao.com/oauth/authorize
            token-uri: https://kauth.kakao.com/oauth/token
            user-info-uri: https://kapi.kakao.com/v2/user/me
            user-name-attribute: (발급된 token으로 userinfo 가져왔을 때 확인 할 key)
```