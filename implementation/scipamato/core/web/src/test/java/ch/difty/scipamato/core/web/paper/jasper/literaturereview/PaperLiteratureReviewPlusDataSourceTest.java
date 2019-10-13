package ch.difty.scipamato.core.web.paper.jasper.literaturereview;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Collections;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignField;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.web.paper.jasper.PaperDataSourceTest;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;

class PaperLiteratureReviewPlusDataSourceTest extends PaperDataSourceTest {

    private static final String FILE_NAME = "paper_literature_review_plus.pdf";

    private static final Long NUMBER = 5L;

    private static final String BRAND           = "brand";
    private static final String CAPTION         = "caption";
    private static final String NUMBER_LABEL    = "numberLabel";
    private static final String PUBMED_BASE_URL = "https://www.ncbi.nlm.nih.gov/pubmed/";

    private       PaperLiteratureReviewPlusDataSource ds;
    private final ReportHeaderFields                  rhf = newReportHeaderFields();

    private ReportHeaderFields newReportHeaderFields() {
        return ReportHeaderFields
            .builder("", BRAND)
            .numberLabel(NUMBER_LABEL)
            .captionLabel(CAPTION)
            .pubmedBaseUrl(PUBMED_BASE_URL)
            .build();
    }

    @Override
    public void setUpHook() {
        when(paperMock.getNumber()).thenReturn(NUMBER);
        when(paperMock.getAuthors()).thenReturn("a");
        when(paperMock.getPublicationYear()).thenReturn(2017);
        when(paperMock.getTitle()).thenReturn("t");
        when(paperMock.getGoals()).thenReturn("g");
        when(paperMock.getDoi()).thenReturn("d");
        when(paperMock.getLocation()).thenReturn("l");
        when(paperMock.getPmId()).thenReturn(1234);
    }

    @SuppressWarnings("SameParameterValue")
    private void assertDataSource(String fileName) throws JRException {
        assertThat(ds.getConnectionProvider()).isNull();
        assertThat(ds
            .getContentDisposition()
            .toString()).isEqualTo("ATTACHMENT");
        assertThat(ds.getContentType()).isEqualTo("application/pdf");
        assertThat(ds.getExtension()).isEqualTo("pdf");
        assertThat(ds.getJasperReport()).isInstanceOf(JasperReport.class);
        assertThat(ds.getReportParameters()).isNotEmpty();
        assertThat(ds
            .getReportParameters()
            .get("show_goal")).isEqualTo(true);

        assertThat(ds.getFileName()).isEqualTo(fileName);

        @SuppressWarnings("SpellCheckingInspection") final JRDataSource jsds = ds.getReportDataSource();
        JRDesignField f = new JRDesignField();

        assertThat(jsds.next()).isTrue();
        assertFieldValue("number", String.valueOf(NUMBER), f, jsds);
        assertFieldValue("authors", "a", f, jsds);
        assertFieldValue("publicationYear", "2017", f, jsds);
        assertFieldValue("title", "t", f, jsds);
        assertFieldValue("goals", "g", f, jsds);
        assertFieldValue("location", "l", f, jsds);
        assertFieldValue("doi", "d", f, jsds);
        assertFieldValue("pubmedLink", "https://www.ncbi.nlm.nih.gov/pubmed/1234", f, jsds);

        assertFieldValue("caption", CAPTION, f, jsds);
        assertFieldValue("brand", BRAND, f, jsds);
        assertFieldValue("numberLabel", NUMBER_LABEL, f, jsds);

        assertThat(jsds.next()).isFalse();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void instantiatingWithProvider_returnsPdfDataSourceWithOneRecord() throws JRException {
        when(dataProviderMock.size()).thenReturn(1L);
        when(dataProviderMock.findAllPapersByFilter()).thenReturn(Collections.singletonList(paperMock));

        ds = new PaperLiteratureReviewPlusDataSource(dataProviderMock, rhf, pdfExporterConfigMock);
        assertDataSource(FILE_NAME);

        verify(dataProviderMock).size();
        verify(dataProviderMock).findAllPapersByFilter();

        verify(paperMock).getNumber();
        verify(paperMock).getAuthors();
        verify(paperMock, times(2)).getPublicationYear();
        verify(paperMock).getTitle();
        verify(paperMock).getGoals();
        verify(paperMock).getLocation();
        verify(paperMock).getDoi();
        verify(paperMock).getPmId();
    }
}
