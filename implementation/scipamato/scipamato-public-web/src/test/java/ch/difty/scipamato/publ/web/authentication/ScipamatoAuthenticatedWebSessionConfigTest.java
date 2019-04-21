package ch.difty.scipamato.publ.web.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.publ.web.WicketTest;

public class ScipamatoAuthenticatedWebSessionConfigTest extends WicketTest {

    @Autowired
    private ScipamatoAuthenticatedWebSessionConfig config;

    @Test
    public void canWire() {
        assertThat(config).isNotNull();
    }

    @Test
    public void providesSecureWebSession() {
        assertThat(config.getAuthenticatedWebSessionClass()).isNotNull();
        assertThat(config
            .getAuthenticatedWebSessionClass()
            .getName()).isEqualTo(SecureWebSession.class.getName());
    }
}
