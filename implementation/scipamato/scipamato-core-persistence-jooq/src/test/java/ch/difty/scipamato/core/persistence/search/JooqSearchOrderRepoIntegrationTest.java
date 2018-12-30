package ch.difty.scipamato.core.persistence.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.JooqTransactionalIntegrationTest;

@SuppressWarnings("SameParameterValue")
public class JooqSearchOrderRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    private static final Integer RECORD_COUNT_PREPOPULATED = 4;
    private static final Long    MAX_ID_PREPOPULATED       = 4L;
    private static final String  LC                        = "de";

    @Autowired
    private JooqSearchOrderRepo repo;

    @Test
    public void findingAll() {
        List<SearchOrder> searchOrders = repo.findAll();
        assertThat(searchOrders).hasSize(RECORD_COUNT_PREPOPULATED);
    }

    @Test
    public void findingById_withExistingId_returnsEntity() {
        SearchOrder searchOrder = repo.findById(RECORD_COUNT_PREPOPULATED.longValue());
        if (MAX_ID_PREPOPULATED > 0)
            assertThat(searchOrder.getId()).isEqualTo(MAX_ID_PREPOPULATED);
        else
            assertThat(searchOrder).isNull();
    }

    @Test
    public void findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1L)).isNull();
    }

    @Test
    public void addingRecord_savesRecordAndRefreshesId() {
        SearchOrder so = makeMinimalSearchOrder();
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setAuthors("foo");
        so.add(searchCondition);
        so.addExclusionOfPaperWithId(4L);
        assertThat(so.getId()).isNull();
        assertThat(so
            .getSearchConditions()
            .get(0)
            .getId()).isNull();
        assertThat(so
            .getSearchConditions()
            .get(0)
            .getStringSearchTerms()
            .iterator()
            .next()
            .getId()).isNull();

        SearchOrder saved = repo.add(so);

        assertThat(saved).isNotNull();
        assertThat(saved.getId())
            .isNotNull()
            .isGreaterThan(MAX_ID_PREPOPULATED);
        assertThat(saved.getOwner()).isEqualTo(10);
        assertThat(saved
            .getSearchConditions()
            .get(0)
            .getSearchConditionId())
            .isNotNull()
            .isGreaterThan(5L);
        assertThat(saved
            .getSearchConditions()
            .get(0)
            .getStringSearchTerms()
            .iterator()
            .next()
            .getId()).isNotNull();

        assertThat(saved.getExcludedPaperIds()).containsOnly(4L);
    }

    private SearchOrder makeMinimalSearchOrder() {
        SearchOrder so = new SearchOrder();
        so.setName(null);
        so.setOwner(10);
        so.setGlobal(true);
        return so;
    }

    @Test
    public void updatingRecord() {
        SearchOrder searchOrder = repo.add(makeMinimalSearchOrder());
        assertThat(searchOrder).isNotNull();
        assertThat(searchOrder.getId())
            .isNotNull()
            .isGreaterThan(MAX_ID_PREPOPULATED);
        final long id = searchOrder.getId();
        assertThat(searchOrder.getOwner()).isEqualTo(10);
        assertThat(searchOrder.getName()).isNull();

        searchOrder.setOwner(20);
        searchOrder.setName("soName");
        repo.update(searchOrder);
        assertThat(searchOrder.getId()).isEqualTo(id);
        assertThat(searchOrder.getName()).isEqualTo("soName");

        SearchOrder newCopy = repo.findById(id);
        assertThat(newCopy).isNotEqualTo(searchOrder);
        assertThat(newCopy.getId()).isEqualTo(id);
        assertThat(newCopy.getOwner()).isEqualTo(20);
        assertThat(newCopy.getName()).isEqualTo("soName");
    }

    @Test
    public void deletingRecord() {
        SearchOrder searchOrder = repo.add(makeMinimalSearchOrder());
        assertThat(searchOrder).isNotNull();
        assertThat(searchOrder.getId())
            .isNotNull()
            .isGreaterThan(MAX_ID_PREPOPULATED);
        final long id = searchOrder.getId();
        assertThat(searchOrder.getOwner()).isEqualTo(10);

        SearchOrder deleted = repo.delete(id, searchOrder.getVersion());
        assertThat(deleted.getId()).isEqualTo(id);

        assertThat(repo.findById(id)).isNull();
    }

    @Test
    public void enrichingAssociatedEntities_hasConditionsAndTerms() {
        final SearchOrder so = new SearchOrder();
        so.setId(1L);
        repo.enrichAssociatedEntitiesOf(so, LC);

        assertThat(so
            .getSearchConditions()
            .size()).isGreaterThanOrEqualTo(2);

        SearchCondition sc1 = so
            .getSearchConditions()
            .get(0);
        assertThat(sc1).isNotNull();
        assertThat(sc1.getAuthors()).isEqualTo("kutlar");
        assertThat(sc1.getDisplayValue()).isEqualTo("kutlar");

        SearchCondition sc2 = so
            .getSearchConditions()
            .get(1);
        assertThat(sc2).isNotNull();
        assertThat(sc2.getAuthors()).isEqualTo("turner");
        assertThat(sc2.getPublicationYear()).isEqualTo("2014-2015");
        assertThat(sc2.getDisplayValue()).isEqualTo("turner AND 2014-2015");
    }

    @Test
    public void enrichingAssociatedEntities_hasExcludedIds() {
        final SearchOrder so = new SearchOrder();
        so.setId(4L);
        repo.enrichAssociatedEntitiesOf(so, LC);

        assertThat(so.getExcludedPaperIds())
            .hasSize(1)
            .containsExactly(1L);
    }

    @Test
    public void addAndModifyAndDeleteSearchConditions() {
        // make search order with single condition (string search term)
        SearchOrder initialSearchOrder = makeMinimalSearchOrder();
        initialSearchOrder.add(newConditionWithAuthors("foo"));
        assertThat(initialSearchOrder.getId()).isNull();

        SearchOrder savedSearchOrder = repo.add(initialSearchOrder);
        // saved search order now has a db-generated id, still has single condition.
        long searchOrderId = savedSearchOrder.getId();
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(1);

        // add additional title condition to existing search order
        SearchCondition titleCondition = newConditionWithTitle("PM2.5");
        SearchCondition savedCondition = repo.addSearchCondition(titleCondition, searchOrderId, LC);
        assertSearchTermCount(1, 0, 0, 0, savedCondition);
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(2);

        // modify the currently savedCondition to also have a publicationYear integer
        // search term
        savedCondition.setPublicationYear("2000");
        SearchCondition modifiedCondition = repo.updateSearchCondition(savedCondition, searchOrderId, LC);
        assertSearchTermCount(1, 1, 0, 0, modifiedCondition);
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(3);

        // modify the integer condition
        savedCondition.setPublicationYear("2001");
        SearchCondition modifiedCondition2 = repo.updateSearchCondition(savedCondition, searchOrderId, LC);
        assertSearchTermCount(1, 1, 0, 0, modifiedCondition2);
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(3);

        // Add boolean condition with Code
        savedCondition.setFirstAuthorOverridden(Boolean.TRUE);
        savedCondition.addCode(new Code("1A", null, null, false, 1, "c1", "", 1));
        SearchCondition modifiedCondition3 = repo.updateSearchCondition(savedCondition, searchOrderId, LC);
        assertSearchTermCount(1, 1, 1, 0, modifiedCondition3);
        assertThat(modifiedCondition3.getCodes()).hasSize(1);
        assertThat(modifiedCondition3.getCodes())
            .extracting(Code.CodeFields.CODE.getName())
            .containsExactly("1A");
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(4);

        // Change the boolean condition
        savedCondition.setFirstAuthorOverridden(Boolean.FALSE);
        SearchCondition modifiedCondition4 = repo.updateSearchCondition(savedCondition, searchOrderId, LC);
        assertSearchTermCount(1, 1, 1, 0, modifiedCondition4);
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(4);

        savedCondition.setModifiedDisplayValue("foo");
        SearchCondition modifiedCondition5 = repo.updateSearchCondition(savedCondition, searchOrderId, LC);
        assertSearchTermCount(1, 1, 1, 2, modifiedCondition5);
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(6);

        savedCondition.setModifiedDisplayValue("bar");
        SearchCondition modifiedCondition6 = repo.updateSearchCondition(savedCondition, searchOrderId, LC);
        assertSearchTermCount(1, 1, 1, 2, modifiedCondition6);
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(6);

        // modify and verify newsletter fields (newsletterTopicId and newsletterHeadline)
        assertThat(savedCondition.getNewsletterTopicId()).isNull();
        savedCondition.setNewsletterTopic(new NewsletterTopic(1, "foo"));
        SearchCondition modifiedCondition7 = repo.updateSearchCondition(savedCondition, searchOrderId, LC);
        assertThat(modifiedCondition7.getNewsletterTopicId()).isEqualTo(1);

        assertThat(savedCondition.getNewsletterHeadline()).isNull();
        savedCondition.setNewsletterHeadline("some");
        SearchCondition modifiedCondition8 = repo.updateSearchCondition(savedCondition, searchOrderId, LC);
        assertThat(modifiedCondition8.getNewsletterHeadline()).isEqualTo("some");

        assertThat(savedCondition.getNewsletterIssue()).isNull();
        savedCondition.setNewsletterIssue("some");
        SearchCondition modifiedCondition9 = repo.updateSearchCondition(savedCondition, searchOrderId, LC);
        assertThat(modifiedCondition9.getNewsletterIssue()).isEqualTo("some");

        // remove the new search condition
        repo.deleteSearchConditionWithId(savedCondition.getSearchConditionId());
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(1);
    }

    private SearchCondition newConditionWithAuthors(String authors) {
        SearchCondition sc = new SearchCondition();
        sc.setAuthors(authors);
        assertSearchTermCount(1, 0, 0, 0, sc);
        return sc;
    }

    private void assertSearchTermCount(int sst, int ist, int bst, int ast, SearchCondition sc) {
        assertThat(sc.getStringSearchTerms()).hasSize(sst);
        assertThat(sc.getIntegerSearchTerms()).hasSize(ist);
        assertThat(sc.getBooleanSearchTerms()).hasSize(bst);
        assertThat(sc.getAuditSearchTerms()).hasSize(ast);
    }

    private SearchCondition newConditionWithTitle(String title) {
        SearchCondition sc = new SearchCondition();
        sc.setTitle(title);
        assertSearchTermCount(1, 0, 0, 0, sc);
        return sc;
    }

    @Test
    public void addingSearchConditionWithoutIdThatAlreadyExists_canReturnUpdatedNewsletterAttributes() {
        // make search order with single condition (string search term)
        SearchOrder initialSearchOrder = makeMinimalSearchOrder();
        initialSearchOrder.add(newConditionWithAuthors("foo"));
        assertThat(initialSearchOrder.getId()).isNull();

        SearchOrder savedSearchOrder = repo.add(initialSearchOrder);
        // saved search order now has a db-generated id, still has single condition.
        long searchOrderId = savedSearchOrder.getId();
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(1);

        // add additional title condition to existing search order
        SearchCondition titleCondition = newConditionWithTitle("PM2.5");
        SearchCondition savedCondition = repo.addSearchCondition(titleCondition, searchOrderId, LC);
        assertSearchTermCount(1, 0, 0, 0, savedCondition);
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(2);

        SearchCondition newButEquivalentCondition = newConditionWithTitle("PM2.5");
        newButEquivalentCondition.setNewsletterIssue("2018/02");
        newButEquivalentCondition.setNewsletterHeadline("someHeadLine");
        newButEquivalentCondition.setNewsletterTopic(new NewsletterTopic(1, "foo"));

        SearchCondition savedNewCondition = repo.addSearchCondition(newButEquivalentCondition, searchOrderId, LC);
        assertThat(savedNewCondition.getNewsletterIssue()).isEqualTo("2018/02");
        assertThat(savedNewCondition.getNewsletterHeadline()).isEqualTo("someHeadLine");
        assertThat(savedNewCondition.getNewsletterTopicId()).isEqualTo(1);

        // remove the new search condition
        repo.deleteSearchConditionWithId(savedCondition.getSearchConditionId());
        assertThat(repo.findConditionIdsWithSearchTerms(searchOrderId)).hasSize(1);
    }

    @Test
    public void removingObsoleteSearchTerms_withNoRemovedKeys_doesNothing() {
        final SearchCondition sc = Mockito.mock(SearchCondition.class);
        when(sc.getRemovedKeys()).thenReturn(Collections.emptySet());

        repo.removeObsoleteSearchTerms(sc, -1L);

        verify(sc, never()).clearRemovedKeys();
    }

    @Test
    public void removingObsoleteSearchTerms_withRemovedKeys_deletesAndClearsKeys() {
        final SearchCondition sc = Mockito.mock(SearchCondition.class);
        when(sc.getRemovedKeys()).thenReturn(Set.of("foo", "bar"));

        repo.removeObsoleteSearchTerms(sc, -1L);

        verify(sc).clearRemovedKeys();
    }
}
