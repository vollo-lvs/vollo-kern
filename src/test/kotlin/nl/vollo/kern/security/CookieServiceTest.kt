package nl.vollo.kern.security

import io.mockk.every
import io.mockk.mockkClass
import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import javax.servlet.http.HttpServletRequest

internal class CookieServiceTest {

    private val cookieService = CookieService()

    private var request: HttpServletRequest = mockkClass(HttpServletRequest::class)

    @Test
    fun nieuweCookie() {
        every { request.isSecure } returns true
        val token = RandomStringUtils.randomAscii(100)

        val cookie = cookieService.nieuweCookie(request, token)

        assertThat(cookie.value, equalTo(token))
        assertThat(cookie.secure, equalTo(true))
        assertThat(cookie.isHttpOnly, equalTo(true))
    }

    @Test
    fun verwijderCookie() {
    }
}