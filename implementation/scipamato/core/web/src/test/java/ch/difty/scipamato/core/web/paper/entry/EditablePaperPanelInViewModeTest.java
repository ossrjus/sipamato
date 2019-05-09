package ch.difty.scipamato.core.web.paper.entry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.markup.html.form.Form;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.search.PaperFilter;

@SuppressWarnings("SpellCheckingInspection")
class EditablePaperPanelInViewModeTest extends EditablePaperPanelTest {

    @Override
    Mode getMode() {
        return Mode.VIEW;
    }

    @Override
    protected void setUpLocalHook() {
        // when referring to PaperSearchPage
        when(searchOrderServiceMock.findById(SEARCH_ORDER_ID)).thenReturn(Optional.empty());
    }

    @Override
    protected void assertSpecificComponents() {
        String b = PANEL_ID;
        getTester().assertComponent(b, EditablePaperPanel.class);

        assertCommonComponents(b);

        b += ":form";
        assertTextFieldWithLabel(b + ":id", 1L, "ID");
        assertTextFieldWithLabel(b + ":number", 100L, "SciPaMaTo-No.");
        assertTextFieldWithLabel(b + ":publicationYear", 2017, "Pub. Year");
        assertTextFieldWithLabel(b + ":pmId", 1234, "PMID");
        getTester().assertInvisible(b + ":submit");
        assertTextFieldWithLabel(b + ":createdDisplayValue", "u1 (2017-02-01 13:34:45)", "Created");
        assertTextFieldWithLabel(b + ":modifiedDisplayValue", "u2 (2017-03-01 13:34:45)", "Last Modified");

        getTester().assertComponent(b + ":back", BootstrapButton.class);
        getTester().assertComponent(b + ":previous", BootstrapButton.class);
        getTester().assertComponent(b + ":next", BootstrapButton.class);
        // disabled as paperManagerMock is not mocked here
        getTester().isDisabled(b + ":previous");
        getTester().isDisabled(b + ":next");

        getTester().assertInvisible(b + ":exclude");

        getTester().assertInvisible(b + ":pubmedRetrieval");

        getTester().assertInvisible(b + ":modAssociation");

        getTester().clickLink("panel:form:tabs:tabs-container:tabs:5:link");

        String bb = b + ":tabs:panel:tab6Form";
        getTester().assertInvisible(bb + ":dropzone");
        getTester().assertComponent(bb + ":attachments", BootstrapDefaultDataTable.class);
        getTester().assertComponent(bb, Form.class);

        verifyCodeAndCodeClassCalls(1, 1);
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

    @Test
    void isAssociatedWithNewsletter_withNewsletterLink() {
        EditablePaperPanel p = makePanel();
        p
            .getModelObject()
            .setNewsletterLink(new Paper.NewsletterLink(1, "i1", 1, 1, "t1", "hl"));
        assertThat(p.isAssociatedWithNewsletter()).isTrue();
    }

    @Test
    void specificFields_areDisabled() {
        getTester().startComponentInPage(makePanel());
        getTester().isDisabled("panel:form:id");
        getTester().isDisabled("panel:form:firstAuthorOverridden");
        getTester().isDisabled("panel:form:createdDisplayValue");
        getTester().isDisabled("panel:form:modifiedDisplayValue");
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
    }

}
