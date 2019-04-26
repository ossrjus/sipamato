package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ch.difty.scipamato.core.ScipamatoCoreApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ScipamatoCoreApplication.class)
public class ScipamatoPropertiesIntegrationTest {

    @Autowired
    private ScipamatoProperties sp;

    @Test
    public void brand_hasDefaultValue() {
        assertThat(sp.getBrand()).isEqualTo("SciPaMaTo");
    }

    @Test
    public void defaultLocalization_hasDefaultEnglish() {
        assertThat(sp.getDefaultLocalization()).isEqualTo("de");
    }

    @Test
    public void pubmedBaseUrl_hasDefaultValue() {
        assertThat(sp.getPubmedBaseUrl()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/");
    }

    @Test
    public void authorParser_isDefault() {
        assertThat(sp.getAuthorParser()).isEqualTo("PUBMED");
    }

    @Test
    public void paperMinimumToBeRecycled_hasDefaultValue() {
        assertThat(sp.getPaperNumberMinimumToBeRecycled()).isEqualTo(8);
    }

    @Test
    public void dbSchema_hasValuePublic() {
        assertThat(sp.getDbSchema()).isEqualTo("public");
    }

    @Test
    public void gettingMultiSelectBoxActionBoxWithMoreEntriesThan_hasDefaultValue4() {
        assertThat(sp.getMultiSelectBoxActionBoxWithMoreEntriesThan()).isEqualTo(4);
    }

    @Test
    public void gettingPubmedApiKey_hasNoDefaultValue() {
        assertThat(sp.getPubmedApiKey()).isNull();
    }
}
