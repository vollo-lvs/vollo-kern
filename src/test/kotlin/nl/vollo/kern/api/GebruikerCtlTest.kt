package nl.vollo.kern.api

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import nl.vollo.kern.model.Gebruiker
import nl.vollo.kern.security.CookieService
import nl.vollo.kern.security.GebruikerAuthenticationService
import nl.vollo.kern.test.isTextNode
import org.apache.commons.lang3.RandomUtils
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.core.MethodParameter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@ExtendWith(MockKExtension::class)
class GebruikerCtlTest {

    private val objectMapper = ObjectMapper()

    @RelaxedMockK
    private lateinit var authentication: GebruikerAuthenticationService

    @SpyK
    private var cookieService: CookieService = CookieService()

    private val gebruiker: Gebruiker = Gebruiker(
            id = RandomUtils.nextLong(), version = 0,
            gebruikersnaam = "tester", wachtwoord = ""
    )

    @InjectMockKs
    private lateinit var gebruikerCtl: GebruikerCtl

    private lateinit var mockMvc: MockMvc

    @BeforeAll
    fun beforeAll() {
        val argumentResolver = object: HandlerMethodArgumentResolver {
            override fun supportsParameter(parameter: MethodParameter) =
                parameter.parameterType == Gebruiker::class.java

            override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? = gebruiker
        }
        mockMvc = MockMvcBuilders
                .standaloneSetup(gebruikerCtl)
                .setCustomArgumentResolvers(argumentResolver)
                .build()
    }

    @Test
    fun `geeft ingelogde gebruiker terug`() {
        val result = mockMvc.perform(get("/gebruiker/ingelogd"))
                .andExpect(status().isOk)
                .andReturn().response

        val json = objectMapper.readTree(result.contentAsString)
        assertThat(json["id"].asLong(), equalTo(gebruiker.id))
        assertThat(json["gebruikersnaam"], isTextNode(gebruiker.gebruikersnaam))
        assertThat(json["_type"], isTextNode(gebruiker._type.name))
    }

    @Test
    fun `uitloggen logt gebruiker uit en geeft lege cookie terug`() {
        val result = mockMvc.perform(post("/gebruiker/uitloggen"))
                .andExpect(status().isNoContent)
                .andReturn().response

        verify { authentication.logout(gebruiker) }
        assertThat(result.getCookie(CookieService.VOLLO_TOKEN_NAME)?.value, equalTo(""))
    }
}