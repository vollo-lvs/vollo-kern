package nl.vollo.kern.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.vollo.kern.model.Gebruiker;
import nl.vollo.kern.security.CookieService;
import nl.vollo.kern.security.TokenAuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("GebruikerCtl")
class GebruikerCtlTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TokenAuthenticationService gebruikerAuthenticationService;

    @Spy
    private CookieService cookieService = new CookieService();

    @InjectMocks
    private GebruikerCtl gebruikerCtl;

    private MockMvc mockMvc;

    private Gebruiker gebruiker = new Gebruiker();

    @BeforeEach
    void beforeAll() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(gebruikerCtl)
                .setCustomArgumentResolvers(maakGebruikerArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("geeft ingelogde gebruiker terug")
    void geeftIngelogdeGebruikerTerug() throws Exception {
        var result = mockMvc.perform(get("/gebruiker/ingelogd"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        var gebruikerResult = objectMapper.readValue(result.getContentAsString(), Gebruiker.class);
        assertThat(gebruikerResult, equalTo(gebruiker));
    }

    @Test
    @DisplayName("uitloggen logt gebruiker uit en geeft lege cookie terug")
    void logtGebruikerUit() throws Exception {
        var result = mockMvc.perform(post("/gebruiker/uitloggen"))
                .andExpect(status().isNoContent())
                .andReturn().getResponse();

        verify(gebruikerAuthenticationService).logout(gebruiker);
        Cookie cookie = result.getCookie(CookieService.VOLLO_TOKEN_NAME);
        assertThat(cookie, notNullValue());
        assertThat(cookie.getValue(), is(""));
    }

    private HandlerMethodArgumentResolver maakGebruikerArgumentResolver() {
        return new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(@NonNull MethodParameter parameter) {
                return parameter.getParameterType().equals(Gebruiker.class);
            }

            @Override
            public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                return gebruiker;
            }
        };
    }

}