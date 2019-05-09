package ch.difty.scipamato.core.entity.projection;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID;
import static ch.difty.scipamato.core.entity.projection.PaperSlim.PaperSlimFields.*;
import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaperSlimTest {

    private PaperSlim ps;

    @BeforeEach
    void setUp() {
        ps = new PaperSlim();
        ps.setId(1L);
        ps.setNumber(10L);
        ps.setFirstAuthor("firstAuthor");
        ps.setTitle("title");
        ps.setPublicationYear(2016);
        ps.setNewsletterAssociation(new NewsletterAssociation(20, "nl", 1, "hl"));
    }

    @Test
    void getting_hasAllFields() {
        getting(20, "nl", 1, "hl");
    }

    private void getting() {
        getting(null, null, null, null);
    }

    private void getting(Integer nlId, String nlIssue, Integer nlStatus, String headline) {
        assertThat(ps.getId()).isEqualTo(1L);
        assertThat(ps.getNumber()).isEqualTo(10L);
        assertThat(ps.getPublicationYear()).isEqualTo(2016);
        assertThat(ps.getTitle()).isEqualTo("title");
        assertThat(ps.getFirstAuthor()).isEqualTo("firstAuthor");
        if (nlId != null) {
            assertThat(ps
                .getNewsletterAssociation()
                .getId()).isEqualTo(nlId);
            assertThat(ps
                .getNewsletterAssociation()
                .getIssue()).isEqualTo(nlIssue);
            assertThat(ps
                .getNewsletterAssociation()
                .getPublicationStatusId()).isEqualTo(nlStatus);
        } else {
            assertThat(ps.getNewsletterAssociation()).isNull();
        }
    }

    @Test
    void displayValue() {
        assertThat(ps.getDisplayValue()).isEqualTo("firstAuthor (2016): title.");
    }

    @Test
    void testingToString() {
        assertThat(ps.toString()).isEqualTo(
            "PaperSlim(number=10, firstAuthor=firstAuthor, publicationYear=2016, title=title, newsletter=nl, headline=hl)");
    }

    @Test
    void testingToString_withNoNewsletter() {
        ps.setNewsletterAssociation(null);
        assertThat(ps.toString()).isEqualTo(
            "PaperSlim(number=10, firstAuthor=firstAuthor, publicationYear=2016, title=title)");
    }

    @Test
    void testingToString_withNoHeadline() {
        ps
            .getNewsletterAssociation()
            .setHeadline(null);
        assertThat(ps.toString()).isEqualTo(
            "PaperSlim(number=10, firstAuthor=firstAuthor, publicationYear=2016, title=title, newsletter=nl)");
    }

    @Test
    void alternativeConstructorwithoutNewsletter_hasAllFields_exceptNewsletter() {
        ps = new PaperSlim(1L, 10L, "firstAuthor", 2016, "title");
        getting(null, null, null, null);
    }

    @Test
    void alternativeConstructor_withNewsletterFields() {
        ps = new PaperSlim(1L, 10L, "firstAuthor", 2016, "title", 20, "nlTitle", 1, "hl");
        getting(20, "nlTitle", 1, "hl");
    }

    @Test
    void alternativeConstructor_withNewsletter() {
        ps = new PaperSlim(1L, 10L, "firstAuthor", 2016, "title", new NewsletterAssociation(30, "t", 3, "headline"));
        getting(30, "t", 3, "headline");
    }

    @Test
    void equals() {
        EqualsVerifier
            .forClass(PaperSlim.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getName(), CREATOR_ID.getName(), MODIFIED.getName(), MODIFIER_ID.getName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void fields() {
        assertThat(PaperSlim.PaperSlimFields.values()).containsExactly(NUMBER, FIRST_AUTHOR, PUBLICATION_YEAR, TITLE,
            NEWSLETTER_ASSOCIATION);
        assertThat(PaperSlim.PaperSlimFields.values())
            .extracting("name")
            .containsExactly("number", "firstAuthor", "publicationYear", "title", "newsletterAssociation");
    }

}
