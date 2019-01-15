package ch.difty.scipamato.core.web.paper.jasper.summarytable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignField;
import org.junit.Test;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.web.paper.jasper.PaperDataSourceTest;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PaperSummaryTableDataSourceTest extends PaperDataSourceTest {

    private static final Long   NUMBER           = 100L;
    private static final String FIRST_AUTHOR     = "firstAuthor";
    private static final int    PUBLICATION_YEAR = 2017;
    private static final String GOALS            = "goals";
    private static final String TITLE            = "title";
    private static final String RESULT           = "result";
    private static final String CODES_OF_CC1     = "1F";
    private static final String CODES_OF_CC4     = "4A,4C";
    private static final String CODES_OF_CC7     = "7B";
    private static final String CAPTION          = "caption";
    private static final String BRAND            = "brand";
    private static final String NUMBER_LABEL     = "nl";

    private static final String FILE_NAME = "paper_summary_table.pdf";

    private final List<Code> codesOfCodeClass1 = new ArrayList<>();
    private final List<Code> codesOfCodeClass4 = new ArrayList<>();
    private final List<Code> codesOfCodeClass7 = new ArrayList<>();

    private PaperSummaryTableDataSource ds;
    private ReportHeaderFields          rhf = newReportHeaderFields();

    private ReportHeaderFields newReportHeaderFields() {
        return ReportHeaderFields
            .builder("", BRAND)
            .numberLabel(NUMBER_LABEL)
            .captionLabel(CAPTION)
            .build();
    }

    @Override
    public void setUpHook() {
        codesOfCodeClass1.add(new Code("1F", "Code1F", "", false, CodeClassId.CC1.getId(), "CC1", "CC1D", 1));
        codesOfCodeClass4.add(new Code("4A", "Code4A", "", false, CodeClassId.CC4.getId(), "CC4", "CC4D", 1));
        codesOfCodeClass4.add(new Code("4C", "Code4C", "", false, CodeClassId.CC4.getId(), "CC4", "CC4D", 3));
        codesOfCodeClass7.add(new Code("7B", "Code7B", "", false, CodeClassId.CC7.getId(), "CC7", "CC7D", 2));

        when(paperMock.getNumber()).thenReturn(NUMBER);
        when(paperMock.getFirstAuthor()).thenReturn(FIRST_AUTHOR);
        when(paperMock.getPublicationYear()).thenReturn(PUBLICATION_YEAR);
        when(paperMock.getGoals()).thenReturn(GOALS);
        when(paperMock.getTitle()).thenReturn(TITLE);
        when(paperMock.getResult()).thenReturn(RESULT);

        when(paperMock.getCodesOf(CodeClassId.CC1)).thenReturn(codesOfCodeClass1);
        when(paperMock.getCodesOf(CodeClassId.CC4)).thenReturn(codesOfCodeClass4);
        when(paperMock.getCodesOf(CodeClassId.CC7)).thenReturn(codesOfCodeClass7);
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
        assertThat(ds.getReportParameters()).isEmpty();

        assertThat(ds.getFileName()).isEqualTo(fileName);

        final JRDataSource jsds = ds.getReportDataSource();
        JRDesignField f = new JRDesignField();

        assertThat(jsds.next()).isTrue();
        assertFieldValue("number", String.valueOf(NUMBER), f, jsds);
        assertFieldValue("firstAuthor", FIRST_AUTHOR, f, jsds);
        assertFieldValue("publicationYear", String.valueOf(PUBLICATION_YEAR), f, jsds);
        assertFieldValue("goals", GOALS, f, jsds);
        assertFieldValue("title", TITLE, f, jsds);
        assertFieldValue("result", RESULT, f, jsds);
        assertFieldValue("codesOfClass1", CODES_OF_CC1, f, jsds);
        assertFieldValue("codesOfClass4", CODES_OF_CC4, f, jsds);
        assertFieldValue("codesOfClass7", CODES_OF_CC7, f, jsds);

        assertFieldValue("caption", CAPTION, f, jsds);
        assertFieldValue("brand", BRAND, f, jsds);

        assertThat(jsds.next()).isFalse();
    }

    @Test
    public void instantiatingWithProvider_returnsPdfDataSourceWithOneRecord() throws JRException {
        when(dataProviderMock.size()).thenReturn(1L);
        when(dataProviderMock.findAllPapersByFilter()).thenReturn(Collections.singletonList(paperMock));

        ds = new PaperSummaryTableDataSource(dataProviderMock, rhf, pdfExporterConfigMock);
        assertDataSource(FILE_NAME);

        verify(dataProviderMock).size();
        verify(dataProviderMock).findAllPapersByFilter();

        verify(paperMock).getNumber();
        verify(paperMock).getFirstAuthor();
        verify(paperMock, times(2)).getPublicationYear();
        verify(paperMock).getGoals();
        verify(paperMock).getTitle();
        verify(paperMock).getResult();
        verify(paperMock).getCodesOf(CodeClassId.CC1);
        verify(paperMock).getCodesOf(CodeClassId.CC4);
        verify(paperMock).getCodesOf(CodeClassId.CC7);
    }

    @Test
    public void instantiatingWithProvider_withEmptyProvider_returnsNoRecord() throws JRException {
        when(dataProviderMock.size()).thenReturn(0L);
        ds = new PaperSummaryTableDataSource(dataProviderMock, rhf, pdfExporterConfigMock);
        assertThat(ds
            .getReportDataSource()
            .next()).isFalse();
        verify(dataProviderMock).size();
    }

    @Test
    public void instantiatingWithProvider_withNullProvider_throws() {
        try {
            new PaperSummaryTableDataSource(null, rhf, pdfExporterConfigMock);
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("dataProvider must not be null.");
        }
    }

}
