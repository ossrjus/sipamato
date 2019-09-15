package ch.difty.scipamato.publ.web.font;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.publ.config.ApplicationPublicProperties;
import ch.difty.scipamato.publ.web.resources.SimplonCssResourceReference;

@ExtendWith(MockitoExtension.class)
class SimplonFontResourceProviderTest {

    private SimplonFontResourceProvider provider;

    @Mock
    private ApplicationPublicProperties applicationProperties;

    @Test
    void degenerateConstruction_withNullArgument() {
        assertDegenerateSupplierParameter(() -> new SimplonFontResourceProvider(null), "applicationProperties");
    }

    @Test
    void withNoCommercialFontPresentSetting_getsNull() {
        when(applicationProperties.isCommercialFontPresent()).thenReturn(false);

        provider = new SimplonFontResourceProvider(applicationProperties);
        assertThat(provider.getCssResourceReference()).isNull();
        assertThat(provider.isCommercialFontPresent()).isFalse();

        verify(applicationProperties).isCommercialFontPresent();
    }

    @Test
    void withCommercialFontPresentSetting_getsReference() {
        when(applicationProperties.isCommercialFontPresent()).thenReturn(true);

        provider = new SimplonFontResourceProvider(applicationProperties);
        assertThat(provider.getCssResourceReference())
            .isNotNull()
            .isInstanceOf(SimplonCssResourceReference.class);
        assertThat(provider.isCommercialFontPresent()).isTrue();

        verify(applicationProperties).isCommercialFontPresent();
    }
}
