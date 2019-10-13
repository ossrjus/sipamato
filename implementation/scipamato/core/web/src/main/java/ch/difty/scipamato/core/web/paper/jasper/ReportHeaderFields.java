package ch.difty.scipamato.core.web.paper.jasper;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.apache.wicket.util.io.IClusterable;
import org.jetbrains.annotations.NotNull;

/**
 * Context holder for localized jasper report captions. Uses builder pattern in
 * order to avoid having constructor with numerous String arguments.
 * <p>
 * This class can be used to serve various jasper reports with different needs
 * for localized labels. Consequently hardly any validation occurs in this
 * class. It is up to the DTOs that are passed into the reports to validate the
 * required labels are non-null.
 *
 * @author u.joss
 */
@Value
@Builder(builderMethodName = "hiddenBuilder")
public class ReportHeaderFields implements IClusterable {

    private static final long serialVersionUID = 1L;

    @NonNull
    private final String headerPart;
    @NonNull
    private final String brand;

    private final String goalsLabel;
    private final String methodsLabel;
    private final String methodOutcomeLabel;
    private final String resultMeasuredOutcomeLabel;
    private final String methodStudyDesignLabel;
    private final String populationPlaceLabel;
    private final String populationParticipantsLabel;
    private final String populationDurationLabel;
    private final String exposurePollutantLabel;
    private final String exposureAssessmentLabel;
    private final String resultExposureRangeLabel;
    private final String methodStatisticsLabel;
    private final String methodConfoundersLabel;
    private final String resultEffectEstimateLabel;
    private final String conclusionLabel;
    private final String commentLabel;

    private final String populationLabel;
    private final String resultLabel;

    private final String captionLabel;
    private final String numberLabel;

    private final String authorYearLabel;

    private final String pubmedBaseUrl;

    /**
     * Static builder requiring the headerPart and brand to be passed into the
     * constructor
     *
     * @param headerPart
     *     the header part as string
     * @param brand
     *     the application brand as string
     * @return ReportHeaderFieldsBuilder
     */
    public static ReportHeaderFieldsBuilder builder(@NotNull final String headerPart, @NotNull final String brand) {
        return hiddenBuilder()
            .headerPart(headerPart)
            .brand(brand);
    }
}
