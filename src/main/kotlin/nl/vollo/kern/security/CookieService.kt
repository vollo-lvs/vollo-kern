package nl.vollo.kern.security

import org.springframework.stereotype.Service
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest

@Service
class CookieService {

    fun nieuweCookie(request: HttpServletRequest, token: String): Cookie {
        val cookie: Cookie = Cookie(VOLLO_TOKEN_NAME, token)
        cookie.isHttpOnly = true
        cookie.secure = request.isSecure
        cookie.path = "/"
        cookie.maxAge = -1
        return cookie;
    }

    fun verwijderCookie(request: HttpServletRequest): Cookie {
        val cookie: Cookie = Cookie(VOLLO_TOKEN_NAME, "")
        cookie.isHttpOnly = true
        cookie.secure = request.isSecure
        cookie.path = "/"
        cookie.maxAge = 0
        return cookie;
    }

    companion object {
        const val VOLLO_TOKEN_NAME = "vollo_token"
    }

}
