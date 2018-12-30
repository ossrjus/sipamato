package ch.difty.scipamato.core.sync.jobs.paper;

import static ch.difty.scipamato.core.db.public_.tables.Paper.PAPER;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jooq.TableField;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.TestUtils;
import ch.difty.scipamato.core.db.public_.tables.records.PaperRecord;

@RunWith(MockitoJUnitRunner.class)
public class SyncShortFieldWithEmptyMainFieldConcatenatorTest {

    private final SyncShortFieldConcatenator sfc = new SyncShortFieldWithEmptyMainFieldConcatenator();

    @Mock
    private ResultSet resultSet;

    @After
    public void tearDown() {
        verifyNoMoreInteractions(resultSet);
    }

    @Test
    public void methods_withNullRecordset_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> sfc.methodsFrom(null), "rs");
    }

    @Test
    public void population_withNullRecordset_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> sfc.populationFrom(null), "rs");
    }

    @Test
    public void result_withNullRecordset_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> sfc.resultFrom(null), "rs");
    }

    @Test
    public void methods_withNonNullMethod_returnsMethod() throws SQLException {
        when(resultSet.getString(PAPER.METHODS.getName())).thenReturn("method");
        when(resultSet.getString(PAPER.METHOD_STUDY_DESIGN.getName())).thenReturn("msd");
        when(resultSet.getString(PAPER.METHOD_OUTCOME.getName())).thenReturn("mo");
        when(resultSet.getString(PAPER.POPULATION_PLACE.getName())).thenReturn("pp");
        when(resultSet.getString(PAPER.EXPOSURE_POLLUTANT.getName())).thenReturn("ep");
        when(resultSet.getString(PAPER.EXPOSURE_ASSESSMENT.getName())).thenReturn("ea");
        when(resultSet.getString(PAPER.METHOD_STATISTICS.getName())).thenReturn("ms");
        when(resultSet.getString(PAPER.METHOD_CONFOUNDERS.getName())).thenReturn("mc");

        assertThat(sfc.methodsFrom(resultSet)).isEqualTo("method");

        verifyCallingMethodsFields();
    }

    private void verifyCallingMethodsFields() throws SQLException {
        verify(resultSet).getString(PAPER.METHODS.getName());
        verify(resultSet).getString(PAPER.METHOD_STUDY_DESIGN.getName());
        verify(resultSet).getString(PAPER.METHOD_OUTCOME.getName());
        verify(resultSet).getString(PAPER.POPULATION_PLACE.getName());
        verify(resultSet).getString(PAPER.EXPOSURE_POLLUTANT.getName());
        verify(resultSet).getString(PAPER.EXPOSURE_ASSESSMENT.getName());
        verify(resultSet).getString(PAPER.METHOD_STATISTICS.getName());
        verify(resultSet).getString(PAPER.METHOD_CONFOUNDERS.getName());
    }

    @Test
    public void methods_withNullMethod_returnsConcatenatedShortMethodFieldsConcatenated() throws SQLException {
        when(resultSet.getString(PAPER.METHODS.getName())).thenReturn(null);
        when(resultSet.getString(PAPER.METHOD_STUDY_DESIGN.getName())).thenReturn("msd");
        when(resultSet.getString(PAPER.METHOD_OUTCOME.getName())).thenReturn("mo");
        when(resultSet.getString(PAPER.POPULATION_PLACE.getName())).thenReturn("pp");
        when(resultSet.getString(PAPER.EXPOSURE_POLLUTANT.getName())).thenReturn("ep");
        when(resultSet.getString(PAPER.EXPOSURE_ASSESSMENT.getName())).thenReturn("ea");
        when(resultSet.getString(PAPER.METHOD_STATISTICS.getName())).thenReturn("ms");
        when(resultSet.getString(PAPER.METHOD_CONFOUNDERS.getName())).thenReturn("mc");

        assertThat(sfc.methodsFrom(resultSet)).isEqualTo(
            "Study Design: msd / Outcome: mo / Place: pp / Pollutant: ep / Exposure Assessment: ea / Statistical Method: ms / Confounders: mc");

        verifyCallingMethodsFields();
    }

    @Test
    public void population_withNonNullPopulation_returnsPopulation() throws SQLException {
        when(resultSet.getString(PAPER.POPULATION.getName())).thenReturn("population");
        when(resultSet.getString(PAPER.POPULATION_PLACE.getName())).thenReturn("ppl");
        when(resultSet.getString(PAPER.POPULATION_PARTICIPANTS.getName())).thenReturn("ppa");
        when(resultSet.getString(PAPER.POPULATION_DURATION.getName())).thenReturn("pd");

        assertThat(sfc.populationFrom(resultSet)).isEqualTo("population");

        verifyCallingPopulationFields();
    }

    private void verifyCallingPopulationFields() throws SQLException {
        verify(resultSet).getString(PAPER.POPULATION.getName());
        verify(resultSet).getString(PAPER.POPULATION_PLACE.getName());
        verify(resultSet).getString(PAPER.POPULATION_PARTICIPANTS.getName());
        verify(resultSet).getString(PAPER.POPULATION_DURATION.getName());
    }

    @Test
    public void population_withNullPopulation_returnsPopulationShortFieldsConcatenated() throws SQLException {
        when(resultSet.getString(PAPER.POPULATION.getName())).thenReturn(null);
        when(resultSet.getString(PAPER.POPULATION_PLACE.getName())).thenReturn("ppl");
        when(resultSet.getString(PAPER.POPULATION_PARTICIPANTS.getName())).thenReturn("ppa");
        when(resultSet.getString(PAPER.POPULATION_DURATION.getName())).thenReturn("pd");

        assertThat(sfc.populationFrom(resultSet)).isEqualTo("Place: ppl / Participants: ppa / Study Duration: pd");

        verifyCallingPopulationFields();
    }

    @Test
    public void result_withNonNullResult_returnsResult() throws SQLException {
        when(resultSet.getString(PAPER.RESULT.getName())).thenReturn("result");
        when(resultSet.getString(PAPER.RESULT_MEASURED_OUTCOME.getName())).thenReturn("rmo");
        when(resultSet.getString(PAPER.RESULT_EXPOSURE_RANGE.getName())).thenReturn("rer");
        when(resultSet.getString(PAPER.RESULT_EFFECT_ESTIMATE.getName())).thenReturn("ree");
        when(resultSet.getString(PAPER.CONCLUSION.getName())).thenReturn("cc");

        assertThat(sfc.resultFrom(resultSet)).isEqualTo("result");

        verifyCallingResultFields();
    }

    private void verifyCallingResultFields() throws SQLException {
        verify(resultSet).getString(PAPER.RESULT.getName());
        verify(resultSet).getString(PAPER.RESULT_MEASURED_OUTCOME.getName());
        verify(resultSet).getString(PAPER.RESULT_EXPOSURE_RANGE.getName());
        verify(resultSet).getString(PAPER.RESULT_EFFECT_ESTIMATE.getName());
        verify(resultSet).getString(PAPER.CONCLUSION.getName());
    }

    @Test
    public void result_withNullResult_returnsResultShortFieldsConcatenated() throws SQLException {
        when(resultSet.getString(PAPER.RESULT.getName())).thenReturn(null);
        when(resultSet.getString(PAPER.RESULT_MEASURED_OUTCOME.getName())).thenReturn("rmo");
        when(resultSet.getString(PAPER.RESULT_EXPOSURE_RANGE.getName())).thenReturn("rer");
        when(resultSet.getString(PAPER.RESULT_EFFECT_ESTIMATE.getName())).thenReturn("ree");
        when(resultSet.getString(PAPER.CONCLUSION.getName())).thenReturn("cc");

        assertThat(sfc.resultFrom(resultSet)).isEqualTo(
            "Measured Outcome: rmo / Exposure (Range): rer / Effect Estimate: ree / Conclusion: cc");

        verifyCallingResultFields();
    }

    final SyncShortFieldConcatenator throwingConcatenator = new SyncShortFieldWithEmptyMainFieldConcatenator() {
        @Override
        String methodsFrom(final ResultSet rs, final TableField<PaperRecord, String> methodField,
            final TableField<PaperRecord, String> methodStudyDesignField,
            final TableField<PaperRecord, String> methodOutcomeField,
            final TableField<PaperRecord, String> populationPlaceField,
            final TableField<PaperRecord, String> exposurePollutantField,
            final TableField<PaperRecord, String> exposureAssessmentField,
            final TableField<PaperRecord, String> methodStatisticsField,
            final TableField<PaperRecord, String> methodConfoundersField) throws SQLException {
            throw new SQLException("boom");
        }

        @Override
        String populationFrom(final ResultSet rs, final TableField<PaperRecord, String> populationField,
            final TableField<PaperRecord, String> populationPlaceField,
            final TableField<PaperRecord, String> populationParticipantsField,
            final TableField<PaperRecord, String> populationDurationField) throws SQLException {
            throw new SQLException("boom");
        }

        @Override
        String resultFrom(final ResultSet rs, final TableField<PaperRecord, String> resultField,
            final TableField<PaperRecord, String> resultMeasuredOutcomeField,
            final TableField<PaperRecord, String> resultExposureRangeField,
            final TableField<PaperRecord, String> resultEffectEstimateField,
            final TableField<PaperRecord, String> conclusionField) throws SQLException {
            throw new SQLException("boom");
        }
    };

    @Test
    public void methodsFrom_withThrowingMethod_returnsNull() {
        assertThat(throwingConcatenator.methodsFrom(mock(ResultSet.class))).isNull();
    }

    @Test
    public void polulationFrom_withThrowingMethod_returnsNull() {
        assertThat(throwingConcatenator.populationFrom(mock(ResultSet.class))).isNull();
    }

    @Test
    public void resultFrom_withThrowingMethod_returnsNull() {
        assertThat(throwingConcatenator.resultFrom(mock(ResultSet.class))).isNull();
    }

}