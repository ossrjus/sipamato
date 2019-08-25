package ch.difty.scipamato.core.web.newsletter.topic;

import static org.mockito.Mockito.*;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.web.authentication.LogoutPage;
import ch.difty.scipamato.core.web.common.BasePageTest;

@SuppressWarnings("SpellCheckingInspection")
class NewsletterTopicEditPageTest extends BasePageTest<NewsletterTopicEditPage> {

    private NewsletterTopicDefinition ntd;

    @Mock
    private NewsletterTopicDefinition newsletterTopicDefinitionDummy;

    @Override
    public void setUpHook() {
        final NewsletterTopicTranslation ntt_de = new NewsletterTopicTranslation(1, "de", "thema1", 1);
        final NewsletterTopicTranslation ntt_en = new NewsletterTopicTranslation(2, "en", "topic1", 1);
        final NewsletterTopicTranslation ntt_fr = new NewsletterTopicTranslation(3, "fr", "sujet1", 1);
        ntd = new NewsletterTopicDefinition(1, "de", 1, ntt_de, ntt_en, ntt_fr);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(newsletterTopicServiceMock);
    }

    @Override
    protected NewsletterTopicEditPage makePage() {
        return new NewsletterTopicEditPage(Model.of(ntd), null);
    }

    @Override
    protected Class<NewsletterTopicEditPage> getPageClass() {
        return NewsletterTopicEditPage.class;
    }

    @Override
    public void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);

        b += ":";

        String bb = b + "header";
        getTester().assertLabel(bb + "Label", "Newsletter Topic");

        bb += "Panel:";
        getTester().assertComponent(bb + "back", BootstrapButton.class);
        getTester().assertComponent(bb + "submit", BootstrapButton.class);
        getTester().assertComponent(bb + "delete", BootstrapButton.class);

        bb = b + "translations";
        getTester().assertLabel(bb + "Label", "Topic Translations");

        bb = b + "translationsPanel:translations";
        getTester().assertComponent(bb, RefreshingView.class);
        bb += ":";
        assertTranslation(bb, 1, "de", "thema1");
        assertTranslation(bb, 2, "en", "topic1");
        assertTranslation(bb, 3, "fr", "sujet1");
    }

    private void assertTranslation(final String bb, final int idx, final String langCode, final String title) {
        getTester().assertLabel(bb + idx + ":langCode", langCode);
        getTester().assertComponent(bb + idx + ":title", TextField.class);
        getTester().assertModelValue(bb + idx + ":title", title);
    }

    @Test
    void submitting_withSuccessfulServiceCall_addsInfoMsg() {
        when(newsletterTopicServiceMock.saveOrUpdate(isA(NewsletterTopicDefinition.class))).thenReturn(ntd);

        runSubmitTest();

        getTester().assertInfoMessages(
            "Successfully saved NewsletterTopic [id 1]: DE: '1806'; EN: 'topic1'; FR: 'sujet1'.");
        getTester().assertNoErrorMessage();
    }

    private void runSubmitTest() {
        getTester().startPage(new NewsletterTopicEditPage(Model.of(ntd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("translationsPanel:translations:1:title", "1806");
        assertTranslation("form:translationsPanel:translations:", 1, "de", "thema1");
        formTester.submit("headerPanel:submit");
        assertTranslation("form:translationsPanel:translations:", 4, "de", "1806");

        verify(newsletterTopicServiceMock).saveOrUpdate(isA(NewsletterTopicDefinition.class));
    }

    @Test
    void submitting_withUnsuccessfulServiceCall_addsErrorMsg() {
        when(newsletterTopicServiceMock.saveOrUpdate(isA(NewsletterTopicDefinition.class))).thenReturn(null);

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("Could not save NewsletterTopic [id 1].");
    }

    @Test
    void submitting_withOptimisticLockingException_addsErrorMsg() {
        when(newsletterTopicServiceMock.saveOrUpdate(isA(NewsletterTopicDefinition.class))).thenThrow(
            new OptimisticLockingException("tblName", "rcd", OptimisticLockingException.Type.UPDATE));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("The tblName with id 1 has been modified concurrently "
                                        + "by another user. Please reload it and apply your changes once more.");
    }

    @Test
    void submitting_withDuplicateKeyException_addsErrorMsg() {
        when(newsletterTopicServiceMock.saveOrUpdate(isA(NewsletterTopicDefinition.class))).thenThrow(
            new DuplicateKeyException("some message about duplicate key stuff"));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("some message about duplicate key stuff");
    }

    @Test
    void submitting_withDuplicateKeyException_withNullMsg_addsStandardErrorMsg() {
        //noinspection ConstantConditions
        when(newsletterTopicServiceMock.saveOrUpdate(isA(NewsletterTopicDefinition.class))).thenThrow(
            new DuplicateKeyException(null));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("Unexpected DuplicateKeyConstraintViolation");
    }

    @Test
    void submitting_withOtherException_addsErrorMsg() {
        when(newsletterTopicServiceMock.saveOrUpdate(isA(NewsletterTopicDefinition.class))).thenThrow(
            new RuntimeException("fooMsg"));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages(
            "An unexpected error occurred when trying to save NewsletterTopic [id 1]: fooMsg");
    }

    @Test
    void submittingDelete_delegatesDeleteToService() {
        when(newsletterTopicServiceMock.delete(anyInt(), anyInt())).thenReturn(newsletterTopicDefinitionDummy);

        getTester().startPage(new NewsletterTopicEditPage(Model.of(ntd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:delete");

        verify(newsletterTopicServiceMock).delete(1, 1);
        verify(newsletterTopicServiceMock, never()).saveOrUpdate(any());

        verify(newsletterTopicServiceMock).countByFilter(isA(NewsletterTopicFilter.class));

        getTester().assertNoInfoMessage();
        getTester().assertNoErrorMessage();
    }

    @Test
    void submitting_withForeignKeyConstraintViolationException_addsErrorMsg() {
        String msg = "...whatever...";
        when(newsletterTopicServiceMock.delete(anyInt(), anyInt())).thenThrow(new DataIntegrityViolationException(msg));

        getTester().startPage(new NewsletterTopicEditPage(Model.of(ntd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:delete");

        verify(newsletterTopicServiceMock).delete(anyInt(), anyInt());

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("Unable to delete this record as it is still used in other places.");
    }

    @Test
    void clickingBackButton_withPageWithoutCallingPageRef_forwardsToNewsletterTopicListPage() {
        getTester().startPage(new NewsletterTopicEditPage(Model.of(ntd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:back");

        getTester().assertRenderedPage(NewsletterTopicListPage.class);

        // from NewsletterListPage
        verify(newsletterTopicServiceMock).countByFilter(isA(NewsletterTopicFilter.class));
    }

    @Test
    void clickingBackButton_withPageWithCallingPageRef_forwardsToThat() {
        getTester().startPage(
            new NewsletterTopicEditPage(Model.of(ntd), new LogoutPage(new PageParameters()).getPageReference()));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:back");

        getTester().assertRenderedPage(LogoutPage.class);
    }

}