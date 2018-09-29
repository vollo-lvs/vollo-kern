package nl.vollo.kern.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.vollo.kern.model.Gebruiker;
import nl.vollo.kern.security.CookieService;
import nl.vollo.kern.security.GebruikerAuthenticationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("GebruikerCtl")
class GebruikerCtlTest {

    private ObjectMapper objectMapper;

    @Mock
    private GebruikerAuthenticationService gebruikerAuthenticationService;

    @Spy
    private CookieService cookieService = new CookieService();

    @InjectMocks
    private GebruikerCtl gebruikerCtl;

    private MockMvc mockMvc;

    private Gebruiker gebruiker = new Gebruiker();

    @BeforeAll
    void beforeAll() {
        var argumentResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterType().equals(Gebruiker.class);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
                return gebruiker;
            }
        };
        mockMvc = MockMvcBuilders
                .standaloneSetup(gebruikerCtl)
                .setCustomArgumentResolvers(argumentResolver)
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
}