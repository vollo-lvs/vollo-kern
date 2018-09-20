package nl.vollo.kern.security

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import java.io.IOException
import java.util.*
import java.util.Optional.ofNullable
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

internal class TokenAuthenticationFilter(requiresAuth: RequestMatcher) : AbstractAuthenticationProcessingFilter(requiresAuth) {

    override fun attemptAuthentication(
            request: HttpServletRequest,
            response: HttpServletResponse): Authentication {
        val param = getVolloCookie(request)
                // TODO weg uit productiecode:
                .orElse(request.getParameter("auth"))

        val token = ofNullable(param)
                .map { it.trim() }
                .orElseThrow { BadCredentialsException("Missing Authentication Token") }

        val auth = UsernamePasswordAuthenticationToken(token, token)
        return authenticationManager.authenticate(auth)
    }

    fun getVolloCookie(request: HttpServletRequest): Optional<String> {
        if (request.cookies != null) {
            for (cookie in request.cookies) {
                if ("vollo_token" == cookie.name) {
                    return ofNullable(cookie.value)
                }
            }
        }
        return Optional.empty()
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
            request: HttpServletRequest,
            response: HttpServletResponse,
            chain: FilterChain?,
            authResult: Authentication) {
        super.successfulAuthentication(request, response, chain, authResult)
        chain!!.doFilter(request, response)
    }

    companion object {
        private val BEARER = "Bearer"
    }
}