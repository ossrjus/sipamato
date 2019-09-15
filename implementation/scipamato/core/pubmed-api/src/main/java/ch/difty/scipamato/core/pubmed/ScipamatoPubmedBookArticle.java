package ch.difty.scipamato.core.pubmed;

import java.util.List;
import java.util.stream.Collectors;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.pubmed.api.AuthorList;
import ch.difty.scipamato.core.pubmed.api.BookDocument;
import ch.difty.scipamato.core.pubmed.api.LocationLabel;
import ch.difty.scipamato.core.pubmed.api.PubmedBookArticle;

/**
 * Derives from {@link AbstractPubmedArticleFacade} wrapping an instance of
 * {@link PubmedBookArticle}.
 *
 * <b>Note:</b> The extraction of the fields implemented so far is purely based
 * upon inspection of the DTD. I'm not sure if the {@link PubmedBookArticle} is
 * relevant for scipamato at all. If it is, we'll need a real example from
 * pubmed and will need to build an integration test to validate the extraction
 * is correct.
 *
 * @author u.joss
 */
class ScipamatoPubmedBookArticle extends AbstractPubmedArticleFacade {

    ScipamatoPubmedBookArticle(final PubmedBookArticle pubmedBookArticle) {
        AssertAs.INSTANCE.notNull(pubmedBookArticle, "pubmedBookArticle");
        final BookDocument bookDocument = AssertAs.INSTANCE.notNull(pubmedBookArticle.getBookDocument(),
            "pubmedBookArticle.bookDocument");
        final List<AuthorList> authorLists = bookDocument.getAuthorList();

        setPmId(bookDocument.getPMID() != null ?
            bookDocument
                .getPMID()
                .getvalue() :
            null);
        if (!authorLists.isEmpty()) {
            AuthorList authorList = authorLists.get(0);
            setAuthors(getAuthorsFrom(authorList));
            setFirstAuthor(getFirstAuthorFrom(authorList));
        }
        setPublicationYear(bookDocument.getContributionDate() != null ?
            bookDocument
                .getContributionDate()
                .getYear()
                .getvalue() :
            null);
        setLocation(getLocationFrom(bookDocument));
        setTitle(bookDocument.getArticleTitle() != null ?
            bookDocument
                .getArticleTitle()
                .getvalue() :
            null);
        setDoi(getDoiFromArticleIdList(bookDocument.getArticleIdList()));
        setOriginalAbstract(getAbstractFrom(bookDocument.getAbstract()));
    }

    private String getLocationFrom(BookDocument bookDocument) {
        return bookDocument
            .getLocationLabel()
            .stream()
            .map(LocationLabel::getvalue)
            .collect(Collectors.joining(" - "));
    }

}
