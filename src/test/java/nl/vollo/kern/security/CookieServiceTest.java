package nl.vollo.kern.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CookieService")
class CookieServiceTest {

    private CookieService cookieService = new CookieService();

    @Mock
    private HttpServletRequest request;

    @Test
    @DisplayName("geeft een nieuwe cookie terug")
    void nieuweCookie() {
        when(request.isSecure()).thenReturn(true);
        var token = RandomStringUtils.randomAscii(100);

        var cookie = cookieService.nieuweCookie(request, token);

        assertThat(cookie.getValue(), is(token));
        assertThat(cookie.getSecure(), is(true));
        assertThat(cookie.isHttpOnly(), is(true));
    }

    @Test
    @DisplayName("geeft een lege cookie terug")
    void verwijderCookie() {
        when(request.isSecure()).thenReturn(true);

        var cookie = cookieService.verwijderCookie(request);

        assertThat(cookie.getValue(), is(""));
        assertThat(cookie.getSecure(), is(true));
        assertThat(cookie.isHttpOnly(), is(true));
    }
}