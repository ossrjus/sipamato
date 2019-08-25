package ch.difty.scipamato.common.web;

import java.util.Locale;

import org.apache.wicket.markup.head.ResourceAggregator;
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest(classes = TestApplication.class)
public abstract class WicketBaseTest {

    protected static final String USERNAME = "testuser";
    protected static final String PASSWORD = "secretpw";

    private WicketTester tester;

    @Autowired
    private WebApplication wicketApplication;

    @Autowired
    private ApplicationContext        applicationContextMock;
    @Autowired
    private ScipamatoWebSessionFacade webSessionFacade;

    @BeforeEach
    void setUp() {
        wicketApplication.setHeaderResponseDecorator(
            r -> new ResourceAggregator(new JavaScriptFilteredIntoFooterHeaderResponse(r, "footer-container")));

        ReflectionTestUtils.setField(wicketApplication, "applicationContext", applicationContextMock);
        tester = new WicketTester(wicketApplication);
        Locale locale = new Locale("en_US");
        tester
            .getSession()
            .setLocale(locale);
        setUpHook();
    }

    /**
     * override if needed
     */
    protected void setUpHook() {
    }

    protected WicketTester getTester() {
        return tester;
    }
}
