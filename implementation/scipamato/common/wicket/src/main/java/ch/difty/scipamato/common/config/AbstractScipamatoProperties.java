package ch.difty.scipamato.common.config;

/**
 * Common abstract base class for ScipamatoProperties in both the core and
 * public module.
 *
 * @param <SP>
 *     the concrete type of the ScipamatoProperties class (extending
 *     {@link ScipamatoBaseProperties}.
 * @author Urs Joss
 */
public abstract class AbstractScipamatoProperties<SP extends ScipamatoBaseProperties> implements ApplicationProperties {

    private final SP              scipamatoProperties;
    private final MavenProperties mavenProperties;

    protected AbstractScipamatoProperties(final SP scipamatoProperties, final MavenProperties mavenProperties) {
        this.scipamatoProperties = scipamatoProperties;
        this.mavenProperties = mavenProperties;
    }

    protected SP getScipamatoProperties() {
        return scipamatoProperties;
    }

    @SuppressWarnings("WeakerAccess")
    MavenProperties getMavenProperties() {
        return mavenProperties;
    }

    @Override
    public String getBuildVersion() {
        return getMavenProperties().getVersion();
    }

    @Override
    public String getDefaultLocalization() {
        return getScipamatoProperties().getDefaultLocalization();
    }

    @Override
    public String getBrand() {
        return getScipamatoProperties().getBrand();
    }

    @Override
    public String getTitleOrBrand() {
        final String pageTitle = getScipamatoProperties().getPageTitle();
        if (pageTitle != null)
            return pageTitle;
        else
            return getBrand();
    }

    @Override
    public String getPubmedBaseUrl() {
        return getScipamatoProperties().getPubmedBaseUrl();
    }

    @Override
    public String getCmsUrlSearchPage() {
        return getScipamatoProperties().getCmsUrlSearchPage();
    }

    @Override
    public Integer getRedirectFromPort() {
        return getScipamatoProperties().getRedirectFromPort();
    }

    @Override
    public int getMultiSelectBoxActionBoxWithMoreEntriesThan() {
        return getScipamatoProperties().getMultiSelectBoxActionBoxWithMoreEntriesThan();
    }
}
