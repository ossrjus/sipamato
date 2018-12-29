package ch.difty.scipamato.core.pubmed;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.core.pubmed.api.*;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class PubmedXmlServiceTest {

    private PubmedXmlService service;

    @Mock
    private Jaxb2Marshaller    unmarshallerMock;
    @Mock
    private PubMed             pubMedMock;
    @Mock
    private PubmedArticleSet   pubmedArticleSetMock;
    @Mock
    private PubmedArticle      pubmedArticleMock;
    @Mock
    private MedlineCitation    medLineCitationMock;
    @Mock
    private Article            articleMock;
    @Mock
    private Journal            journalMock;
    @Mock
    private PMID               pmidMock;
    @Mock
    private JournalIssue       journalIssueMock;
    @Mock
    private PubDate            pubDateMock;
    @Mock
    private MedlineJournalInfo medLineJournalInfoMock;
    @Mock
    private ArticleTitle       articleTitleMock;

    @Before
    public void setUp() {
        service = new PubmedXmlService(unmarshallerMock, pubMedMock);

        when(pubmedArticleMock.getMedlineCitation()).thenReturn(medLineCitationMock);
        when(medLineCitationMock.getArticle()).thenReturn(articleMock);
        when(articleMock.getJournal()).thenReturn(journalMock);
        when(medLineCitationMock.getPMID()).thenReturn(pmidMock);
        when(journalMock.getJournalIssue()).thenReturn(journalIssueMock);
        when(journalIssueMock.getPubDate()).thenReturn(pubDateMock);
        when(medLineCitationMock.getMedlineJournalInfo()).thenReturn(medLineJournalInfoMock);
        when(articleMock.getArticleTitle()).thenReturn(articleTitleMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(unmarshallerMock, pubMedMock, pubmedArticleSetMock);
    }

    @Test
    public void degenerateConstruction_nullUnmarshaller_throws() {
        assertDegenerateSupplierParameter(() -> new PubmedXmlService(null, pubMedMock), "unmarshaller");
    }

    @Test
    public void degenerateConstruction_nullPubMed_throws() {
        assertDegenerateSupplierParameter(() -> new PubmedXmlService(unmarshallerMock, null), "pubMed");
    }

    @Test
    public void unmarshallingNull_throws() {
        try {
            service.unmarshal(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("xmlString must not be null.");
        }
    }

    @Test
    public void gettingPubmedArticleWithPmid_withValidId_returnsArticle() {
        final int pmId = 25395026;
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenReturn(pubmedArticleSetMock);
        final List<java.lang.Object> objects = new ArrayList<>();
        objects.add(pubmedArticleMock);
        when(pubmedArticleSetMock.getPubmedArticleOrPubmedBookArticle()).thenReturn(objects);

        Optional<PubmedArticleFacade> pa = service.getPubmedArticleWithPmid(pmId);
        assertThat(pa.isPresent()).isTrue();
        assertThat(pa.get()).isNotNull();

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
        verify(pubmedArticleSetMock).getPubmedArticleOrPubmedBookArticle();
    }

    @Test
    public void gettingPubmedArticleWithPmid_withInvalidId_returnsEmptyOptional() {
        final int pmId = 999999999;
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenReturn(pubmedArticleSetMock);
        final List<java.lang.Object> objects = new ArrayList<>();
        when(pubmedArticleSetMock.getPubmedArticleOrPubmedBookArticle()).thenReturn(objects);

        Optional<PubmedArticleFacade> pa = service.getPubmedArticleWithPmid(pmId);
        assertThat(pa.isPresent()).isFalse();

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
        verify(pubmedArticleSetMock).getPubmedArticleOrPubmedBookArticle();
    }

    @Test
    public void gettingPubmedArticleWithPmid_withNullObjects_returnsEmptyOptional() {
        final int pmId = 999999999;
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenReturn(pubmedArticleSetMock);
        when(pubmedArticleSetMock.getPubmedArticleOrPubmedBookArticle()).thenReturn(null);

        Optional<PubmedArticleFacade> pa = service.getPubmedArticleWithPmid(pmId);
        assertThat(pa.isPresent()).isFalse();

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
        verify(pubmedArticleSetMock).getPubmedArticleOrPubmedBookArticle();
    }

    @Test
    public void gettingPubmedArticleWithPmidAndApiKey_withValidId_returnsArticle() {
        final int pmId = 25395026;
        when(pubMedMock.articleWithId(String.valueOf(pmId), "key")).thenReturn(pubmedArticleSetMock);
        final List<java.lang.Object> objects = new ArrayList<>();
        objects.add(pubmedArticleMock);
        when(pubmedArticleSetMock.getPubmedArticleOrPubmedBookArticle()).thenReturn(objects);

        Optional<PubmedArticleFacade> pa = service.getPubmedArticleWithPmidAndApiKey(pmId, "key");
        assertThat(pa.isPresent()).isTrue();
        assertThat(pa.get()).isNotNull();

        verify(pubMedMock).articleWithId(String.valueOf(pmId), "key");
        verify(pubmedArticleSetMock).getPubmedArticleOrPubmedBookArticle();
    }

    @Test
    public void nonValidXml_returnsNull() throws XmlMappingException, IOException {
        assertThat(service.unmarshal("")).isNull();
        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    @Test
    public void gettingPubmedArticle_withInvalidId_returnsEmptyArticle() {
        final int pmId = 25395026;
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenThrow(new RuntimeException("boom"));
        final List<java.lang.Object> objects = new ArrayList<>();
        objects.add(pubmedArticleMock);

        Optional<PubmedArticleFacade> pa = service.getPubmedArticleWithPmid(pmId);
        assertThat(pa.isPresent()).isFalse();

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
    }

    @Test
    public void gettingArticles_withUnmarshallerException_returnsEmptyList() {
        when(unmarshallerMock.unmarshal(isA(StreamSource.class))).thenThrow(new UnmarshallingFailureException("boom"));
        assertThat(service.extractArticlesFrom("some invalid xml")).isEmpty();
        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    @Test
    public void gettingArticles_withPubmedArticleSetWithoutArticleCollection_returnsEmptyList() {
        PubmedArticleSet pubmedArticleSet = new PubmedArticleSet();
        when(unmarshallerMock.unmarshal(isA(StreamSource.class))).thenReturn(pubmedArticleSet);
        assertThat(service.extractArticlesFrom("some valid xml")).isEmpty();
        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    @Test
    public void gettingArticles_withPubmedArticleSetWithoutArticleCollection2_returnsEmptyList() {
        when(unmarshallerMock.unmarshal(isA(StreamSource.class))).thenReturn(makeMinimalValidPubmedArticleSet());
        assertThat(service.extractArticlesFrom("some valid xml")).isNotEmpty();
        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    public static PubmedArticleSet makeMinimalValidPubmedArticleSet() {
        PubmedArticleSet pubmedArticleSet = new PubmedArticleSet();
        pubmedArticleSet
            .getPubmedArticleOrPubmedBookArticle()
            .add(ScipamatoPubmedArticleTest.makeMinimalValidPubmedArticle());
        return pubmedArticleSet;
    }

    @Test
    public void gettingPubmedArticleWithPmid_withNoNetwork_returnsEmptyOptional() {
        final int pmId = 25395026;
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenThrow(
            new RuntimeException("The network is not reachable"));

        Optional<PubmedArticleFacade> pa = service.getPubmedArticleWithPmid(pmId);
        assertThat(pa.isPresent()).isFalse();

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
    }

}
