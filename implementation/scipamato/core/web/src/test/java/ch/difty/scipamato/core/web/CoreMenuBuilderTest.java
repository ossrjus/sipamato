package ch.difty.scipamato.core.web;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.common.web.pages.MenuBuilder;
import ch.difty.scipamato.core.web.common.BasePage;

class CoreMenuBuilderTest extends WicketTest {

    @Mock
    private ApplicationProperties     applicationProperties;
    @Mock
    private ScipamatoWebSessionFacade webSessionFacade;
    @Mock
    private Navbar                    navbar;
    @Mock
    private BasePage<?>               basePage;

    private MenuBuilder menuBuilder;

    @Override
    public void setUpHook() {
        menuBuilder = new CoreMenuBuilder(applicationProperties, webSessionFacade);
    }

    @Test
    void degenerateConstruction_withNullApplicationProperties() {
        assertDegenerateSupplierParameter(() -> new CoreMenuBuilder(null, webSessionFacade), "applicationProperties");
    }

    @Test
    void degenerateConstruction_withNullWebSessionFacade() {
        assertDegenerateSupplierParameter(() -> new CoreMenuBuilder(applicationProperties, null), "webSessionFacade");
    }

    @Test
    void degenerateMethodCall_withNullNavbar() {
        try {
            menuBuilder.addMenuLinksTo(null, basePage);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("navbar must not be null.");
        }
    }

    @Test
    void degenerateMethodCall_withNullPage() {
        try {
            menuBuilder.addMenuLinksTo(navbar, null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("page must not be null.");
        }
    }

    // TODO complete the test to verify the actual menu creation. From the original
    // TestAbstractPage

}
