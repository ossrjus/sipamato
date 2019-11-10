package ch.difty.scipamato.publ.config;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.config.ScipamatoBaseProperties;

@Component
@ConfigurationProperties(prefix = "scipamato")
@Getter
@Setter
public class ScipamatoProperties implements ScipamatoBaseProperties {

    private static final long serialVersionUID = 1L;

    /**
     * Brand name of the application. Appears e.g. in the Navbar.
     */
    @NotNull
    private String brand = "SciPaMaTo-Public";

    /**
     * Page Title of the application. Appears in the browser tab.
     */
    @Nullable
    private String pageTitle;

    /**
     * Default localization. Normally the browser locale is used.
     */
    @NotNull
    private String defaultLocalization = "en";

    /**
     * The base url used to access the Pubmed API.
     */
    @NotNull
    private String pubmedBaseUrl = "https://www.ncbi.nlm.nih.gov/pubmed/";

    /**
     * Port from where an unsecured http connection is forwarded to the secured
     * https port (@literal server.port}. Only has an effect if https is configured.
     */
    @Nullable
    private Integer redirectFromPort;

    /**
     * Indicates whether the commercial font MetaOT is present and can be used.
     * <p>
     * SciPaMaTo expects to find the font in the following location:
     *
     * <pre>
     * src/main/resources/ch/difty/scipamato/publ/web/resources/fonts/MetaOT/
     * </pre>
     */
    private boolean commercialFontPresent;

    /**
     * Indicates whether SciPaMaTo should compile the LESS sources into CSS (if
     * {@literal true}) or use the pre-compiled CSS classes (if {@literal false}).
     */
    private boolean lessUsedOverCss;

    /**
     * Indicates if the Navbar is visible by default. Can be overridden via page
     * parameter.
     */
    private boolean navbarVisibleByDefault;

    /**
     * The URL of the CMS page that points to the paper search page
     */
    @Nullable
    private String cmsUrlSearchPage;

    /**
     * The URL of the CMS page that points to the new study page
     */
    @Nullable
    private String cmsUrlNewStudyPage;

    /**
     * The number of characters an abbreviated authors string will have at most. If
     * set to 0: do not abbreviate at all, return full authors string.
     */
    private int authorsAbbreviatedMaxLength;

    /**
     * set to true if SciPaMaTo-Public needs to add pym.js to its page headers. This
     * allows embedding scipamato into responsive iframes.
     */
    private boolean responsiveIframeSupportEnabled;

    /**
     * The user with which you can authenticate to see the actuator end points.
     */
    @NotNull
    private String managementUserName = "admin";

    /**
     * The password of the user with which you can authenticate to see the actuator end points.
     */
    @Nullable
    private String managementUserPassword;

    /**
     * The new study page will list that many newsletters in the archive section (default: 14)
     */
    private int numberOfPreviousNewslettersInArchive = 14;

    /**
     * The threshold above which the multi-select box may activate the actionBox (all/none buttons)
     */
    private int multiSelectBoxActionBoxWithMoreEntriesThan = 4;
}
