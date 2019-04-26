package ch.difty.scipamato.common;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TranslationUtilsTest extends FinalClassTest<TranslationUtils> {

    @Test
    public void verifyNotTranslatedString() {
        assertThat(TranslationUtils.NOT_TRANSL).isEqualTo("not translated");
    }

    @Test
    public void trimmingLanguageCode_withNullCode_throws() {
        assertDegenerateSupplierParameter(() -> TranslationUtils.trimLanguageCode(null), "languageCode");
    }

    @Test
    public void trimmingLanguageCode_with2CharCode_returnsInput() {
        assertThat(TranslationUtils.trimLanguageCode("de")).isEqualTo("de");
        assertThat(TranslationUtils.trimLanguageCode("fr")).isEqualTo("fr");
    }

    @Test
    public void trimmingLanguageCode_withMoreThan2CharCode_returnsFirstTwoChars() {
        assertThat(TranslationUtils.trimLanguageCode("de_CH")).isEqualTo("de");
        assertThat(TranslationUtils.trimLanguageCode("en_US")).isEqualTo("en");
    }

    @Test
    public void deCamelCasing_null_returnsNull() {
        assertThat(TranslationUtils.deCamelCase(null)).isNull();
    }

    @Test
    public void deCamelCasing_blank_returnsBlank() {
        assertThat(TranslationUtils.deCamelCase("")).isEqualTo("");
    }

    @Test
    public void deCamelCasing_withCase_converts() {
        assertThat(TranslationUtils.deCamelCase("fooBar")).isEqualTo("foo_bar");
    }

    @Test
    public void deCamelCasing_withoutCase_returnsOriginal() {
        assertThat(TranslationUtils.deCamelCase("foobar")).isEqualTo("foobar");
    }

    @Test
    public void deCamelCasing_withoutCaseAndUnderscore_returnsOriginal() {
        assertThat(TranslationUtils.deCamelCase("foo_bar")).isEqualTo("foo_bar");
    }

}
