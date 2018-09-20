package nl.vollo.kern

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

internal class VolloConfigTest {

    @Test
    fun `should return default config`() {
        val result = VolloConfig()

        assertThat(result.jwt.issuer, equalTo("vollo"))
        assertThat(result.jwt.expiration.seconds > 0, equalTo(true))
        assertThat(result.jwt.clockSkew.seconds > 0, equalTo(true))
    }
}