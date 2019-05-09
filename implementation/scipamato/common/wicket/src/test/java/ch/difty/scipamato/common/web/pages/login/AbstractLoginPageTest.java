package ch.difty.scipamato.common.web.pages.login;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.WicketBaseTest;

class AbstractLoginPageTest extends WicketBaseTest {

    private AbstractLoginPage page;

    @MockBean
    private ApplicationProperties applicationProperties;
    @MockBean
    private DateTimeService       dateTimeService;

    @Override
    protected void setUpHook() {
        page = new TestLoginPage(new PageParameters());
    }

    @Test
    void assertPage() {
        getTester().startPage(page);
        getTester().assertRenderedPage(AbstractLoginPage.class);

        getTester().assertInvisible("navbar");
        getTester().assertComponent("feedback", NotificationPanel.class);
        getTester().assertComponent("form", StatelessForm.class);
        getTester().assertLabel("form:usernameLabel", "Username");
        getTester().assertComponent("form:username", RequiredTextField.class);
        getTester().assertLabel("form:passwordLabel", "Password");
        getTester().assertComponent("form:password", PasswordTextField.class);
        getTester().assertLabel("form:signin:label", "Sign in");
    }

    @Test
    void submitting_withoutLoginData_rendersErrorMessages() {
        getTester().startPage(page);
        getTester().assertRenderedPage(AbstractLoginPage.class);
        getTester().submitForm("form");

        getTester().assertRenderedPage(AbstractLoginPage.class);

        getTester().assertErrorMessages("'Username' is required.", "'Password' is required.");
    }

    @Test
    void submitting_withInvalidCredentials_doesNotLoginAndRendersErrorMessage() {
        getTester().startPage(page);
        getTester().assertRenderedPage(TestLoginPage.class);

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("username", USERNAME);
        formTester.setValue("password", "Invalid");

        formTester.submit();

        getTester().assertErrorMessages("The username and/or password were invalid.");
    }

    @Test
    void submitting_withValidCredentials_doesLoginAndForwardsToTestHomePage() {
        getTester().startPage(page);
        getTester().assertRenderedPage(TestLoginPage.class);

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("username", USERNAME);
        formTester.setValue("password", PASSWORD);

        formTester.submit();

        getTester().assertNoErrorMessage();
    }

}
