package ch.difty.scipamato.core.entity.newsletter

import ch.difty.scipamato.common.entity.AbstractDefinitionTranslation
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class NewsletterTopicDefinitionTest {

    private val ntt_de = NewsletterTopicTranslation(10, "de", "thema2", 1)
    private val ntt_en = NewsletterTopicTranslation(11, "en", "topic2", 1)
    private val ntt_fr = NewsletterTopicTranslation(12, "fr", "sujet2", 1)

    @Test
    fun withNoTranslations_unableToEstablishMainTitle() {
        val ntd = NewsletterTopicDefinition(1, "de", 1)
        assertThat(ntd.id).isEqualTo(1)
        assertThat(ntd.name).isEqualTo("n.a.")
        assertThat(ntd.displayValue).isEqualTo("n.a.")
        assertThat(ntd.translations.asMap()).isEmpty()
    }

    @Test
    fun withTranslations() {
        val ntd = NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr)
        assertThat(ntd.id).isEqualTo(2)
        assertThat(ntd.name).isEqualTo("thema2")
        assertThat(ntd.displayValue).isEqualTo("thema2")
        assertThat(ntd.translations.asMap()).hasSize(3)
        assertThat(ntd.translations.keySet()).containsExactly("de", "en", "fr")
        val trs = ntd.translations.values()
        assertThat(trs).extracting(AbstractDefinitionTranslation.DefinitionTranslationFields.NAME.fieldName).containsOnly("thema2", "topic2", "sujet2")
        for (tr in trs)
            assertThat(tr.lastModified).isNull()
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation() {
        val ntd = NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr)
        assertThat(ntd.translationsAsString).isEqualTo("DE: 'thema2'; EN: 'topic2'; FR: 'sujet2'")
    }

    @Test
    fun canGetTranslationsAsString_withTranslationsIncludingMainTranslation_withPartialTranslation() {
        val ntd = NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en,
                NewsletterTopicTranslation(12, "fr", null, 1))
        assertThat(ntd.translationsAsString).isEqualTo("DE: 'thema2'; EN: 'topic2'; FR: n.a.")
    }

    @Test
    fun modifyTranslation_withMainLanguageTranslationModified_changesMainTitle_translationTitle_andSetsModifiedTimestamp() {
        val ntd = NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr)
        ntd.setNameInLanguage("de", "thema 2")
        assertThat(ntd.name).isEqualTo("thema 2")
        assertThat(ntd.translations.get("de")[0].name).isEqualTo("thema 2")
        assertThat(ntd.translations.get("de")[0].lastModified).isNotNull()
        assertThat(ntd.translations.get("en")[0].lastModified).isNull()
        assertThat(ntd.translations.get("fr")[0].lastModified).isNull()
    }

    @Test
    fun modifyTranslation_withNonMainLanguageTranslationModified_keepsMainTitle_changesTranslationTitle_andSetsModifiedTimestamp() {
        val ntd = NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr)
        ntd.setNameInLanguage("fr", "bar")
        assertThat(ntd.name).isEqualTo("thema2")
        assertThat(ntd.translations.get("fr")[0].name).isEqualTo("bar")
        assertThat(ntd.translations.get("de")[0].lastModified).isNull()
        assertThat(ntd.translations.get("en")[0].lastModified).isNull()
        assertThat(ntd.translations.get("fr")[0].lastModified).isNotNull()
    }

    @Test
    fun gettingNullSafeId_withNonNullId() {
        val ntd = NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr)
        assertThat(ntd.nullSafeId).isEqualTo(2)
    }

    @Test
    fun gettingNullSafeId_withNullId() {
        val ntd = NewsletterTopicDefinition(null, "de", 1, ntt_de, ntt_en, ntt_fr)
        assertThat(ntd.nullSafeId).isEqualTo(0)
    }

    @Test
    fun titleIsAliasForName() {
        val ntd = NewsletterTopicDefinition(2, "de", 1, ntt_de, ntt_en, ntt_fr)
        assertThat(ntd.title).isEqualTo(ntd.name)
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo(ntd.getNameInLanguage("de"))
        ntd.title = "foo"
        assertThat(ntd.name).isEqualTo("foo")
        ntd.setTitleInLanguage("de", "bar")
        assertThat(ntd.name).isEqualTo("bar")
    }
}