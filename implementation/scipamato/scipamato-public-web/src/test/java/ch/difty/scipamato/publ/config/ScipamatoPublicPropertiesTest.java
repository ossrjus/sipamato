package ch.difty.scipamato.publ.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.config.MavenProperties;

@ExtendWith(MockitoExtension.class)
public class ScipamatoPublicPropertiesTest {

    private ScipamatoPublicProperties prop;

    @Mock
    private ScipamatoProperties scipamatoPropMock;
    @Mock
    private MavenProperties     mavenPropMock;

    @BeforeEach
    public void setUp() {
        prop = new ScipamatoPublicProperties(scipamatoPropMock, mavenPropMock);

        when(scipamatoPropMock.getBrand()).thenReturn("brand");
        when(scipamatoPropMock.getDefaultLocalization()).thenReturn("dl");
        when(scipamatoPropMock.getPubmedBaseUrl()).thenReturn("pbUrl");
        when(scipamatoPropMock.getRedirectFromPort()).thenReturn(5678);

        when(mavenPropMock.getVersion()).thenReturn("0.0.1-SNAPSHOT");

        when(scipamatoPropMock.getCmsUrlSearchPage()).thenReturn("http://u.sp");
        when(scipamatoPropMock.getCmsUrlNewStudyPage()).thenReturn("http://u.nsp");

        when(scipamatoPropMock.getAuthorsAbbreviatedMaxLength()).thenReturn(70);

        when(scipamatoPropMock.getManagementUserName()).thenReturn("un");
        when(scipamatoPropMock.getManagementUserPassword()).thenReturn("pw");

        when(scipamatoPropMock.getNumberOfPreviousNewslettersInArchive()).thenReturn(14);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(scipamatoPropMock, mavenPropMock);
    }

    @Test
    public void gettingBrand_delegatesToScipamatoProps() {
        assertThat(prop.getBrand()).isEqualTo("brand");
        verify(scipamatoPropMock).getBrand();
    }

    @Test
    public void gettingTitleOrBrand_withPageTitleDefined_delegatesToScipamatoProps_andReturnsPageTitle() {
        when(scipamatoPropMock.getPageTitle()).thenReturn("pt");
        assertThat(prop.getTitleOrBrand()).isEqualTo("pt");
        verify(scipamatoPropMock).getPageTitle();
        verify(scipamatoPropMock, never()).getBrand();
    }

    @Test
    public void gettingTitleOrBrand_withPageTitleNotDefined_delegatesToScipamatoProps_andReturnsBrand() {
        when(scipamatoPropMock.getPageTitle()).thenReturn(null);
        assertThat(prop.getTitleOrBrand()).isEqualTo("brand");
        verify(scipamatoPropMock).getPageTitle();
        verify(scipamatoPropMock).getBrand();
    }

    @Test
    public void gettingDefaultLocalization_delegatesToScipamatoProps() {
        assertThat(prop.getDefaultLocalization()).isEqualTo("dl");
        verify(scipamatoPropMock).getDefaultLocalization();
    }

    @Test
    public void gettingPubmedBaseUrl_delegatesToScipamatoProps() {
        assertThat(prop.getPubmedBaseUrl()).isEqualTo("pbUrl");
        verify(scipamatoPropMock).getPubmedBaseUrl();
    }

    @Test
    public void checkingCommercialFontPresence_ifPresent_delegatesToMavenProp() {
        when(scipamatoPropMock.isCommercialFontPresent()).thenReturn(true);
        assertThat(prop.isCommercialFontPresent()).isEqualTo(true);
        verify(scipamatoPropMock).isCommercialFontPresent();
    }

    @Test
    public void checkingCommercialFontPresence_ifNotPresent_delegatesToMavenProp() {
        when(scipamatoPropMock.isCommercialFontPresent()).thenReturn(false);
        assertThat(prop.isCommercialFontPresent()).isEqualTo(false);
        verify(scipamatoPropMock).isCommercialFontPresent();
    }

    @Test
    public void gettingRedirectFromPort_delegatesToScipamatoProp() {
        assertThat(prop.getRedirectFromPort()).isEqualTo(5678);
        verify(scipamatoPropMock).getRedirectFromPort();
    }

    @Test
    public void gettingBuildVersion_delegatesToMavenProp() {
        assertThat(prop.getBuildVersion()).isEqualTo("0.0.1-SNAPSHOT");
        verify(mavenPropMock).getVersion();
    }

    @Test
    public void checkingLessOverCSS_ifTrue_delegatesToScipamatoProp() {
        when(scipamatoPropMock.isLessUsedOverCss()).thenReturn(true);
        assertThat(prop.isLessUsedOverCss()).isEqualTo(true);
        verify(scipamatoPropMock).isLessUsedOverCss();
    }

    @Test
    public void checkingLessOverCSS_ifNotPresent_delegatesToScipamatoProp() {
        when(scipamatoPropMock.isLessUsedOverCss()).thenReturn(false);
        assertThat(prop.isLessUsedOverCss()).isEqualTo(false);
        verify(scipamatoPropMock).isLessUsedOverCss();
    }

    @Test
    public void checkingNavbarDefaultVisibility_delegatesToScipamatoProp() {
        when(scipamatoPropMock.isNavbarVisibleByDefault()).thenReturn(true);
        assertThat(prop.isNavbarVisibleByDefault()).isEqualTo(true);
        verify(scipamatoPropMock).isNavbarVisibleByDefault();
    }

    @Test
    public void checkingCssUrlSearchPage_delegatesToScipamatoProp() {
        assertThat(prop.getCmsUrlSearchPage()).isEqualTo("http://u.sp");
        verify(scipamatoPropMock).getCmsUrlSearchPage();
    }

    @Test
    public void checkingCssUrlNewStudyPage_delegatesToScipamatoProp() {
        assertThat(prop.getCmsUrlNewStudyPage()).isEqualTo("http://u.nsp");
        verify(scipamatoPropMock).getCmsUrlNewStudyPage();
    }

    @Test
    public void checkingAuthorsAbbreviatedMaxLength() {
        assertThat(prop.getAuthorsAbbreviatedMaxLength()).isEqualTo(70);
        verify(scipamatoPropMock).getAuthorsAbbreviatedMaxLength();
    }

    @Test
    public void checkingManagementUserName_delegatesToScipamatoProp() {
        assertThat(prop.getManagementUserName()).isEqualTo("un");
        verify(scipamatoPropMock).getManagementUserName();
    }

    @Test
    public void checkingManagementPassword_delegatesToScipamatoProp() {
        assertThat(prop.getManagementUserPassword()).isEqualTo("pw");
        verify(scipamatoPropMock).getManagementUserPassword();
    }

    @Test
    public void checkingNumberOfPreviousNewslettersInArchive() {
        assertThat(prop.getNumberOfPreviousNewslettersInArchive()).isEqualTo(14);
        verify(scipamatoPropMock).getNumberOfPreviousNewslettersInArchive();
    }

}
