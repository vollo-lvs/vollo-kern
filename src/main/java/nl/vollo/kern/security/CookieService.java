package nl.vollo.kern.security;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
public class CookieService {

    public static final String VOLLO_TOKEN_NAME = "vollo_token";

    public Cookie nieuweCookie(HttpServletRequest request, String token) {
        Cookie cookie = new Cookie(VOLLO_TOKEN_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        return cookie;
    }

    public Cookie verwijderCookie(HttpServletRequest request) {
        Cookie cookie = new Cookie(VOLLO_TOKEN_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

}
