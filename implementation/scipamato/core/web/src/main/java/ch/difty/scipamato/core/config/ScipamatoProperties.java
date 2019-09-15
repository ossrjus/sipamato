package ch.difty.scipamato.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.config.ScipamatoBaseProperties;
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy;

@Component
@ConfigurationProperties(prefix = "scipamato")
@Getter
@Setter
public class ScipamatoProperties implements ScipamatoBaseProperties {

    private static final long serialVersionUID = 1L;

    private static final String AUTHOR_PARSER_PROPERTY_KEY = "scipamato.author-parser";

    /**
     * Brand name of the application. Appears e.g. in the Navbar.
     */
    private String brand = "SciPaMaTo-Core";

    /**
     * Page Title of the application. Appears in the browser tab.
     */
    private String pageTitle;

    /**
     * Default localization. Normally the browser locale is used.
     */
    private String defaultLocalization = "en";

    /**
     * The base url used to access the Pubmed API.
     */
    private String pubmedBaseUrl = "https://www.ncbi.nlm.nih.gov/pubmed/";

    /**
     * The author parser used for parsing Author Strings. Currently only
     * {@literal DEFAULT} is implemented.
     */
    private String authorParser = "DEFAULT";

    /**
     * Any free number below this threshold will not be reused.
     */
    private int paperNumberMinimumToBeRecycled;

    /**
     * DB Schema.
     */
    private String dbSchema = "public";

    /**
     * Port from where an unsecured http connection is forwarded to the secured
     * https port (@literal server.port}. Only has an effect if https is configured.
     */
    private Integer redirectFromPort;

    /**
     * @return the author parser strategy used for interpreting the authors string.
     */
    public AuthorParserStrategy getAuthorParserStrategy() {
        return AuthorParserStrategy.Companion.fromProperty(authorParser, AUTHOR_PARSER_PROPERTY_KEY);
    }

    /**
     * The threshold above which the multi-select box may (if configured) show the
     *     action box providing the select all/select none buttons
     */
    private int multiSelectBoxActionBoxWithMoreEntriesThan = 4;

    /**
     * The API Key used for accessing pubmed papers.
     *
     * https://ncbiinsights.ncbi.nlm.nih.gov/2017/11/02/new-api-keys-for-the-e-utilities/
     */
    private String pubmedApiKey;
}
