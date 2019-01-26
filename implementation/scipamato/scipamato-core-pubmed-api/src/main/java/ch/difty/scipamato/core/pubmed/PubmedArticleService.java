package ch.difty.scipamato.core.pubmed;

import java.util.List;

import ch.difty.scipamato.common.NullArgumentException;

/**
 * Service to handle articles from PubMed and present it in a SciPaMaTo specific
 * representation.
 *
 * @author u.joss
 */
public interface PubmedArticleService {

    /**
     * Retrieve the pubmed article with the provided pmID directly from PubMed.
     *
     * @param pmId
     *     pubmedId
     * @return {@link PubmedArticleResult} holding the {@link PubmedArticleFacade} and some status information
     */
    PubmedArticleResult getPubmedArticleWithPmid(int pmId);

    /**
     * Retrieve the pubmed article with the provided pmID directly from PubMed, using
     * the provided apiKey.
     *
     * @param pmId
     *     pubmedId
     * @param apiKey
     *     the PubmedApi Key - must not be null
     * @return PubmedArticleResult holding the PubmedArticleFacade and some status information
     */
    PubmedArticleResult getPubmedArticleWithPmidAndApiKey(int pmId, String apiKey);

    /**
     * Extracts pubmed articles and pubmed book articles from a a source string. It
     * is the implementation that determines the type and format of the content.
     *
     * @param content
     *     pubmed content, as String. Must not be null.
     * @return List of {@link PubmedArticleFacade} entries. Never null. Will be
     *     empty if there are issues parsing the content.
     * @throws NullArgumentException
     *     in case of null content.
     */
    List<PubmedArticleFacade> extractArticlesFrom(String content);

}
