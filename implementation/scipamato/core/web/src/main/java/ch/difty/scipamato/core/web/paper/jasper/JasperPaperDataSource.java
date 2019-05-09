package ch.difty.scipamato.core.web.paper.jasper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.wicketstuff.jasperreports.JRConcreteResource;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperSlimFilter;
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider;

/**
 * Common base class for Jasper paper data sources.
 *
 * @param <E>
 *     the type of the {@link JasperEntity}
 * @author u.joss
 */
public abstract class JasperPaperDataSource<E extends JasperEntity>
    extends JRConcreteResource<ScipamatoPdfResourceHandler> {

    private static final long serialVersionUID = 1L;

    private final Collection<E>                                        jasperEntities = new ArrayList<>();
    private final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider;
    private final String                                               baseName;

    /**
     * Instantiation of the data source with a list of jasper entities.
     *
     * @param handler
     *     the pdf resource handler used for exporting the pdf
     * @param baseName
     *     the file name without the extension (.pdf)
     * @param jasperEntities
     *     a collection of {@link JasperEntity} items that will be used for
     *     populating the report.
     */
    protected JasperPaperDataSource(final ScipamatoPdfResourceHandler handler, final String baseName,
        final Collection<E> jasperEntities) {
        super(handler);
        this.baseName = AssertAs.notNull(baseName, "baseName");
        this.jasperEntities.clear();
        this.jasperEntities.addAll(AssertAs.notNull(jasperEntities, "jasperEntities"));
        this.dataProvider = null;
        init();
    }

    /**
     * Instantiation of the data source with a data provider (which is capable of
     * fetching the records on its own).
     *
     * @param handler
     *     the pdf resource handler used for exporting the pdf
     * @param baseName
     *     the file name without the extension (.pdf)
     * @param dataProvider
     *     a data provider deriving from {@link AbstractPaperSlimProvider}
     */
    protected JasperPaperDataSource(final ScipamatoPdfResourceHandler handler, final String baseName,
        final AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider) {
        super(handler);
        this.baseName = AssertAs.notNull(baseName, "baseName");
        this.jasperEntities.clear();
        this.dataProvider = AssertAs.notNull(dataProvider, "dataProvider");
        init();
    }

    private void init() {
        setJasperReport(getReport());
        setReportParameters(getParameterMap());
        setFileName(baseName + "." + getExtension());
    }

    // override if needed
    protected HashMap<String, Object> getParameterMap() {
        return new HashMap<>();
    }

    protected abstract JasperReport getReport();

    @Override
    public JRDataSource getReportDataSource() {
        fetchEntitiesFromDataProvider();
        return new JRBeanCollectionDataSource(jasperEntities);
    }

    private void fetchEntitiesFromDataProvider() {
        if (dataProvider != null) {
            jasperEntities.clear();
            if (dataProvider.size() > 0)
                for (final Paper p : dataProvider.findAllPapersByFilter())
                    jasperEntities.add(makeEntity(p));
        }
    }

    /**
     * Implement to instantiate an entity {@code E} from the provided {@link Paper}
     * and additional information required to build it.
     *
     * @param p
     *     the Paper
     * @return the entity
     */
    protected abstract E makeEntity(final Paper p);

    /**
     * Overriding in order to not use the deprecated and incompatible methods still
     * used in JRResource (exporter.setParameter)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected byte[] getExporterData(final JasperPrint print, final JRAbstractExporter exporter) throws JRException {
        // prepare a stream to trap the exporter's output
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

        // execute the export and return the trapped result
        exporter.exportReport();

        return baos.toByteArray();
    }

}
