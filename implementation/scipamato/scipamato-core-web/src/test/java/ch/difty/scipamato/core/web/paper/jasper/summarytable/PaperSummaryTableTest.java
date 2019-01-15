package ch.difty.scipamato.core.web.paper.jasper.summarytable;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.core.web.paper.jasper.JasperEntityTest;
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields;

public class PaperSummaryTableTest extends JasperEntityTest {

    private static final String BRAND        = "brand";
    private static final String CAPTION      = "caption";
    private static final String NUMBER_LABEL = "nl";

    private PaperSummaryTable  pst;
    private ReportHeaderFields rhf = newReportHeaderFields();

    @Test(expected = NullArgumentException.class)
    public void degenerateConstruction_withNullPaper() {
        new PaperSummaryTable(null, rhf);
    }

    @Test(expected = NullArgumentException.class)
    public void degenerateConstruction_withNullReportHeaderFields() {
        new PaperSummaryTable(p, null);
    }

    @Test
    public void instantiating() {
        pst = new PaperSummaryTable(p, rhf);
        assertPst();
    }

    private ReportHeaderFields newReportHeaderFields() {
        return ReportHeaderFields
            .builder(HEADER_PART, BRAND)
            .captionLabel(CAPTION)
            .methodsLabel(METHODS_LABEL)
            .numberLabel(NUMBER_LABEL)
            .build();
    }

    private void assertPst() {
        assertThat(pst.getCaption()).isEqualTo(CAPTION);
        assertThat(pst.getBrand()).isEqualTo(BRAND);
        assertThat(pst.getNumberLabel()).isEqualTo(NUMBER_LABEL);

        assertThat(pst.getNumber()).isEqualTo(String.valueOf(NUMBER));
        assertThat(pst.getFirstAuthor()).isEqualTo(FIRST_AUTHOR);
        assertThat(pst.getPublicationYear()).isEqualTo(String.valueOf(PUBLICATION_YEAR));
        assertThat(pst.getGoals()).isEqualTo(GOALS);
        assertThat(pst.getTitle()).isEqualTo(TITLE);

        assertThat(pst.getCodesOfClass1()).isEqualTo("1F");
        assertThat(pst.getCodesOfClass4()).isEqualTo("4A,4C");
        assertThat(pst.getCodesOfClass7()).isEqualTo("7B");
    }

    @Test
    public void constructionWithPaperWithNoCodeOfClass7_returnsBlank() {
        p.clearCodes();
        pst = new PaperSummaryTable(p, rhf);
        assertThat(pst.getCodesOfClass4()).isEqualTo("");
    }

    @Test
    public void paperWithNullNumber_resultsInEmptyNumber() {
        p.setNumber(null);
        pst = new PaperSummaryTable(p, rhf);
        assertThat(pst.getNumber()).isEmpty();
    }

    @Test
    public void paperWithNullYear_resultsInEmptyYear() {
        p.setPublicationYear(null);
        pst = new PaperSummaryTable(p, rhf);
        assertThat(pst.getPublicationYear()).isEmpty();
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(PaperSummaryTable.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void testingToString() {
        pst = new PaperSummaryTable(p, rhf);
        assertThat(pst.toString()).isEqualTo(
            "PaperSummaryTable(number=100, firstAuthor=firstAuthor, publicationYear=2017, codesOfClass1=1F, codesOfClass4=4A,4C, codesOfClass7=7B, goals=goals, title=title, result=results, caption=caption, brand=brand, numberLabel=nl)");
    }
}
