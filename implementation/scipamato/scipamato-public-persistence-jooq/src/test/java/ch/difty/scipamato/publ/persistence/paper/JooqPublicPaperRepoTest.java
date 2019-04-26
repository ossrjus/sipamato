package ch.difty.scipamato.publ.persistence.paper;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.publ.db.tables.Paper;
import ch.difty.scipamato.publ.db.tables.records.PaperRecord;
import ch.difty.scipamato.publ.entity.PublicPaper;

@ExtendWith(MockitoExtension.class)
class JooqPublicPaperRepoTest {

    private JooqPublicPaperRepo repo;

    @Mock
    private DSLContext                                      dslMock;
    @Mock
    private JooqSortMapper<PaperRecord, PublicPaper, Paper> sortMapperMock;
    @Mock
    private PublicPaperFilterConditionMapper                filterConditionMapperMock;
    @Mock
    private AuthorsAbbreviator                              authorsAbbreviator;
    @Mock
    private JournalExtractor                                journalExtractor;

    @BeforeEach
    void setUp() {
        repo = new JooqPublicPaperRepo(dslMock, sortMapperMock, filterConditionMapperMock, authorsAbbreviator,
            journalExtractor) {
            @Override
            protected String getMainLanguage() {
                return "de";
            }
        };
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(dslMock, sortMapperMock, filterConditionMapperMock);
    }

    @Test
    void findingByNumber_withNullNumber_throws() {
        assertDegenerateSupplierParameter(() -> repo.findByNumber(null), "number");
    }

    @Test
    void mapping_withPaperRecordHandingBackNullEvenForAuditDates_doesNotThrow() {
        PaperRecord pr = mock(PaperRecord.class);
        PublicPaper pp = repo.map(pr);
        assertThat(pp.getCreated()).isNull();
        assertThat(pp.getLastModified()).isNull();
    }

    @Test
    void mapping_callsAuthorsAbbreviator_withAuthors() {
        final String authors = "authors";
        final String authorsAbbr = "auths";
        PaperRecord pr = mock(PaperRecord.class);
        when(pr.getAuthors()).thenReturn(authors);
        when(authorsAbbreviator.abbreviate(authors)).thenReturn(authorsAbbr);

        PublicPaper pp = repo.map(pr);

        assertThat(pp.getAuthors()).isEqualTo(authors);
        assertThat(pp.getAuthorsAbbreviated()).isEqualTo(authorsAbbr);

        verify(authorsAbbreviator).abbreviate(authors);
    }

    @Test
    void mapping_callsJournalExtractor_withLocation() {
        final String location = "location";
        final String journal = "journal";
        PaperRecord pr = mock(PaperRecord.class);
        when(pr.getLocation()).thenReturn(location);
        when(journalExtractor.extractJournal(location)).thenReturn(journal);

        PublicPaper pp = repo.map(pr);

        assertThat(pp.getLocation()).isEqualTo(location);
        assertThat(pp.getJournal()).isEqualTo(journal);

        verify(journalExtractor).extractJournal(location);
    }
}
