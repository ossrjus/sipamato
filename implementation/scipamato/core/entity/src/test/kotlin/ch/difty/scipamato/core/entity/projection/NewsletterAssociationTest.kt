package ch.difty.scipamato.core.entity.projection

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NewsletterAssociationTest {

    private var na = NewsletterAssociation()

    @BeforeEach
    fun setUp() {
        na.id = 1
        na.issue = "issue"
        na.publicationStatusId = 2
        na.headline = "hl"
    }

    @Test
    fun getting() {
        assertThat(na.id).isEqualTo(1)
        assertThat(na.issue).isEqualTo("issue")
        assertThat(na.publicationStatusId).isEqualTo(2)
        assertThat(na.headline).isEqualTo("hl")
    }

    @Test
    fun displayValue() {
        assertThat(na.displayValue).isEqualTo("issue")
    }

    @Test
    fun testingToString() {
        assertThat(na.toString()).isEqualTo("NewsletterAssociation(issue=issue, publicationStatusId=2, headline=hl)")
    }

    @Test
    fun alternativeConstructor() {
        na = NewsletterAssociation(1, "issue", 2, "hl")
        getting()
    }

    @Test
    fun equals() {
        EqualsVerifier
                .forClass(NewsletterAssociation::class.java)
                .withRedefinedSuperclass()
                .usingGetClass()
                .withIgnoredFields(CREATED.fieldName, CREATOR_ID.fieldName, MODIFIED.fieldName, MODIFIER_ID.fieldName)
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify()
    }

    @Test
    fun assertEnumFields() {
        assertThat(NewsletterAssociation.NewsletterAssociationFields.values().map { it.fieldName }).containsExactly("id", "issue", "publicationStatusId", "headline")
    }
}