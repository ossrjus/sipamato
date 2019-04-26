package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy;

public class ScipamatoPropertiesTest {

    private final ScipamatoProperties sp = new ScipamatoProperties();

    @Test
    public void brand_hasDefaultValue() {
        assertThat(sp.getBrand()).isEqualTo("SciPaMaTo-Core");
    }

    @Test
    public void defaultLocalization_hasDefaultEnglish() {
        assertThat(sp.getDefaultLocalization()).isEqualTo("en");
    }

    @Test
    public void pubmedBaseUrl_hasDefaultValue() {
        assertThat(sp.getPubmedBaseUrl()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/");
    }

    @Test
    public void authorParser_isDefault() {
        assertThat(sp.getAuthorParser()).isEqualTo("DEFAULT");
    }

    @Test
    public void authorParserStrategy_isDefault() {
        assertThat(sp.getAuthorParserStrategy()).isEqualTo(AuthorParserStrategy.PUBMED);
    }

    @Test
    public void paperMinimumToBeRecycled_hasDefaultValue() {
        assertThat(sp.getPaperNumberMinimumToBeRecycled()).isEqualTo(0);
    }

    @Test
    public void dbSchema_hasDefaultValuePublic() {
        assertThat(sp.getDbSchema()).isEqualTo("public");
    }

    @Test
    public void gettingRedirectPort_hasNoDefaultValue() {
        assertThat(sp.getRedirectFromPort()).isNull();
    }

    @Test
    public void gettingMultiSelectBoxActionBoxWithMoreEntriesThan_hasDefaultValue() {
        assertThat(sp.getMultiSelectBoxActionBoxWithMoreEntriesThan()).isEqualTo(4);
    }

    @Test
    public void gettingPubmedApiKey_hasNoDefaultValue() {
        assertThat(sp.getPubmedApiKey()).isNull();
    }
}
