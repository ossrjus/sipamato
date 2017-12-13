package ch.difty.scipamato.core.pubmed;

import java.util.List;
import java.util.stream.Collectors;

import ch.difty.scipamato.common.AssertAs;

/**
 * Derives from {@link PubmedArticleFacade} wrapping an instance of {@link PubmedBookArticle}.
 * <p>
 * <b>Note:</b> The extraction of the fields implemented so far is purely based upon inspection of the DTD.
 * I'm not sure if the {@link PubmedBookArticle} is relevant for scipamato at all. If it is, we'll need a real
 * example from pubmed and will need to build an integration test to validate the extraction is correct.
 *
 * @author u.joss
 */
public class ScipamatoPubmedBookArticle extends PubmedArticleFacade {

    protected ScipamatoPubmedBookArticle(final PubmedBookArticle pubmedBookArticle) {
        AssertAs.notNull(pubmedBookArticle, "pubmedBookArticle");
        final BookDocument bookDocument = AssertAs.notNull(pubmedBookArticle.getBookDocument(), "pubmedBookArticle.bookDocument");
        final List<AuthorList> authorLists = bookDocument.getAuthorList();

        setPmId(bookDocument.getPMID() != null ? bookDocument.getPMID().getvalue() : null);
        if (!authorLists.isEmpty()) {
            AuthorList authorList = authorLists.get(0);
            setAuthors(getAuthorsFrom(authorList));
            setFirstAuthor(getFirstAuthorFrom(authorList));
        }
        setPublicationYear(bookDocument.getContributionDate() != null ? bookDocument.getContributionDate().getYear().getvalue() : null);
        setLocation(getLocationFrom(bookDocument));
        setTitle(bookDocument.getArticleTitle() != null ? bookDocument.getArticleTitle().getvalue() : null);
        setDoi(getDoiFromArticleIdList(bookDocument.getArticleIdList()));
        setOriginalAbstract(getAbstractFrom(bookDocument.getAbstract()));
    }

    private String getLocationFrom(BookDocument bookDocument) {
        return bookDocument.getLocationLabel().stream().map(LocationLabel::getvalue).collect(Collectors.joining(" - "));
    }

}