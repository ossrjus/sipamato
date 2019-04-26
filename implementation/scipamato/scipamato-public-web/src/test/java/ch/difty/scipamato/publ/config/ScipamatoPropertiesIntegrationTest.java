package ch.difty.scipamato.publ.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ch.difty.scipamato.publ.ScipamatoPublicApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ScipamatoPublicApplication.class)
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
    public void responsiveIFrameSupport_isDisabled() {
        assertThat(sp.isResponsiveIframeSupportEnabled()).isFalse();
    }

    @Test
    public void multiSelectBoxActionBoxWithMoreEntriesThan_hasDefault4() {
        assertThat(sp.getMultiSelectBoxActionBoxWithMoreEntriesThan()).isEqualTo(4);
    }
}
