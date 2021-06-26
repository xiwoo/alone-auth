package com.inminhouse.alone.auth.config.security.utils;

import java.util.Base64;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.SerializationUtils;

public class CookieUtils {

	public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
		//cookie 배열 꺼내기
		Cookie[] cookies = request.getCookies();
		
		if (cookies != null && cookies.length > 0) {//꺼낸 cookie 배열이 있으면
			for (Cookie cookie : cookies) {//하나씩 꺼내서 for문
				if (cookie.getName().equals(name)) {//꺼낸 쿠키와 넘어온 name이 같으면
					return Optional.of(cookie);//이름이 같은 cookie를 Optional에 담아 return
				}
			}
		}
		
		return Optional.empty(); //조건에 맞는 cookie가 없을 시 빈 Optional을 return
	}
	
	//response에 쿠키 담기
	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
	
	//request에서 쿠키 빼기
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }
    
    //직렬화
    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(object));
    }

	//직렬화 풀기
	public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
    		SerializationUtils.deserialize(
				Base64.getUrlDecoder().decode(cookie.getValue())
			)
		);
    }
}
