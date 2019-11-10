package ch.difty.scipamato.core.web.resources.jasper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;

import java.io.InputStream;

import net.sf.jasperreports.engine.JRException;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

@SuppressWarnings({ "ResultOfMethodCallIgnored", "CatchMayIgnoreException" })
class PaperLiteratureReviewReportResourceReferenceTest
    extends JasperReportResourceReferenceTest<PaperLiteratureReviewReportResourceReference> {

    @Override
    protected PaperLiteratureReviewReportResourceReference getResourceReference() {
        return PaperLiteratureReviewReportResourceReference.get();
    }

    @Override
    protected String getReportBaseName() {
        return "paper_literature_review_A4";
    }

    @Override
    protected String getResourceReferencePath() {
        return "ch.difty.scipamato.core.web.resources.jasper.PaperLiteratureReviewReportResourceReference";
    }

    @Test
    void gettingResourceStream_withNullStream() {
        final JasperReportResourceReference rr = new JasperReportResourceReference(
            PaperLiteratureReviewReportResourceReference.class, "baz", false) {
            @Nullable
            @Override
            IResourceStream getResourceStreamFromResource() {
                return null;
            }
        };

        try {
            rr.getReport();
            fail("should have thrown exception.");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(JasperReportException.class)
                .hasMessage("Unable to locate resource stream for jasper file 'baz.jrxml'");
        }
    }

    @Test
    void gettingResourceStream_withResourceStreamNotFoundException() {
        final JasperReportResourceReference rr = new JasperReportResourceReference(
            PaperLiteratureReviewReportResourceReference.class, "baz", false) {

            @NotNull
            @Override
            IResourceStream getResourceStreamFromResource() {
                return mock(IResourceStream.class);
            }

            @NotNull
            @Override
            InputStream getInputStream(final IResourceStream rs) throws ResourceStreamNotFoundException {
                throw new ResourceStreamNotFoundException("boom");
            }
        };

        try {
            rr.getReport();
            fail("should have thrown exception.");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(JasperReportException.class)
                .hasMessage("org.apache.wicket.util.resource.ResourceStreamNotFoundException: boom");
        }
    }

    @Test
    void compilingReport_throwingJRException() {
        final JasperReportResourceReference rr = new JasperReportResourceReference(
            PaperLiteratureReviewReportResourceReference.class, "baz", false) {
            @Override
            void compileReport() throws JRException {
                throw new JRException("boom");
            }
        };

        try {
            rr.getReport();
            fail("should have thrown exception.");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(JasperReportException.class)
                .hasMessage("net.sf.jasperreports.engine.JRException: boom");
        }
    }
}
