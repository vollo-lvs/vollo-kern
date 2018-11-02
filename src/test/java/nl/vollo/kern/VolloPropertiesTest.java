package nl.vollo.kern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@DisplayName("VolloProperties")
class VolloPropertiesTest {

    @Test
    @DisplayName("geeft de default config terug")
    void getJwt() {
        var jwt = new VolloProperties().getJwt();

        assertThat(jwt.getIssuer(), is(VolloProperties.ISSUER));
        assertThat(jwt.getExpiration().getSeconds() > 0, is(true));
        assertThat(jwt.getClockSkew().getSeconds() > 0, is(true));
    }
}
