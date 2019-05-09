package ch.difty.scipamato.core.web.paper.jasper.summary;

import java.util.Collection;
import java.util.Collections;

import net.sf.jasperreports.engine.JasperReport;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperSlimFilter;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider;
import ch.difty.scipamato.core.web.paper.jasper.*;
import ch.difty.scipamato.core.web.resources.jasper.PaperSummaryReportResourceReference;

/**
 * DataSource for the PaperSummaryReport.
 * <p>
 * Can be instantiated in different ways, either by passing in
 *
 * <ul>
 * <li>a single {@link Paper} + report header fields + export config</li>
 * <li>a single {@link PaperSummary} + export config</li>
 * <li>a collection of {@link PaperSummary} entities + export config or</li>
 * <li>an instance of a {@link AbstractPaperSlimProvider} + report header fields
 * + export config</li>
 * </ul>
 * <p>
 * The report header fields are not contained within a paper instance and make
 * up e.g. localized labels, the brand or part of the header.
 *
 * @author u.joss
 */
public class PaperSummaryDataSource extends JasperPaperDataSource<PaperSummary> {

    private static final long serialVersionUID = 1L;

    private static final String BASE_NAME_SINGLE          = "paper_summary_no_";
    private static final String BASE_NAME_SINGLE_FALLBACK = "paper_summary";
    private static final String BASE_NAME_MULTIPLE        = "paper_summaries";

    private ReportHeaderFields         reportHeaderFields;
    private CoreShortFieldConcatenator shortFieldConcatenator = null;

    /**
     * Build up the paper summary from a {@link Paper} and any additional
     * information not contained within the paper
     *
     * @param paper
     *     an instance of {@link Paper} - must not be null.
     * @param reportHeaderFields
     *     collection of localized labels for report fields
     * @param shortFieldConcatenator
     *     utility bean to manage the content of the fields method/population/result
     * @param config
     *     {@link ClusterablePdfExporterConfiguration}
     */
    public PaperSummaryDataSource(final Paper paper, final ReportHeaderFields reportHeaderFields,
        final CoreShortFieldConcatenator shortFieldConcatenator, ClusterablePdfExporterConfiguration config) {
        this(Collections.singletonList(new PaperSummary(AssertAs.notNull(paper, "paper"), shortFieldConcatenator,
                AssertAs.notNull(reportHeaderFields, "reportHeaderFields"))), config,
            makeSinglePaperBaseName(paper.getNumber() != null ? String.valueOf(paper.getNumber()) : null));
        this.reportHeaderFields = reportHeaderFields;
        this.shortFieldConcatenator = shortFieldConcatenator;
    }

    /**
     * Populate the report from a single {@link PaperSummary}, using a specific file
     * name including the id of the paper.
     *
     * @param paperSummary
     *     single {@link PaperSummary} - must not be null
     * @param config
     *     the {@link ClusterablePdfExporterConfiguration}
     */
    PaperSummaryDataSource(final PaperSummary paperSummary, ClusterablePdfExporterConfiguration config) {
        this(Collections.singletonList(AssertAs.notNull(paperSummary, "paperSummary")), config,
            makeSinglePaperBaseName(paperSummary.getNumber()));
    }

    /**
     * Populate the report from a collection of {@link PaperSummary PaperSummaries},
     * using a specific file name including the id of the paper.
     *
     * @param paperSummaries
     *     collection of {@link PaperSummary} instances
     * @param config
     *     the {@link ClusterablePdfExporterConfiguration}
     * @param baseName
     *     the file name without the extension (.pdf)
     */
    private PaperSummaryDataSource(final Collection<PaperSummary> paperSummaries,
        ClusterablePdfExporterConfiguration config, String baseName) {
        super(new ScipamatoPdfResourceHandler(config), baseName, paperSummaries);
    }

    /**
     * Using the dataProvider for the Result Panel as record source. Needs the
     * {@link PaperService} to retrieve the papers based on the ids of the
     * {@link PaperSlim}s that are used in the dataProvider.
     *
     * @param dataProvider
     *     the {@link AbstractPaperSlimProvider} - must not be null
     * @param reportHeaderFields
     *     collection of localized labels for the report fields
     * @param shortFieldConcatenator
     *     utility bean to manage the content of the fields method/population/result
     * @param config
     *     {@link ClusterablePdfExporterConfiguration}
     */
    public PaperSummaryDataSource(final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider,
        final ReportHeaderFields reportHeaderFields, final CoreShortFieldConcatenator shortFieldConcatenator,
        ClusterablePdfExporterConfiguration config) {
        super(new ScipamatoPdfResourceHandler(config), BASE_NAME_MULTIPLE, dataProvider);
        this.reportHeaderFields = reportHeaderFields;
        this.shortFieldConcatenator = shortFieldConcatenator;
    }

    @Override
    protected JasperReport getReport() {
        return PaperSummaryReportResourceReference
            .get()
            .getReport();
    }

    @Override
    protected PaperSummary makeEntity(Paper p) {
        return new PaperSummary(p, shortFieldConcatenator, reportHeaderFields);
    }

    private static String makeSinglePaperBaseName(String number) {
        if (number != null && !number.isEmpty()) {
            return BASE_NAME_SINGLE + number;
        } else {
            return BASE_NAME_SINGLE_FALLBACK;
        }
    }

}
