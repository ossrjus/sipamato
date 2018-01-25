package ch.difty.scipamato.core.persistence.paper.searchorder;

import static org.assertj.core.api.Assertions.assertThat;

import org.jooq.Condition;
import org.junit.Test;

import ch.difty.scipamato.core.entity.filter.BooleanSearchTerm;
import ch.difty.scipamato.core.entity.filter.SearchTermType;
import ch.difty.scipamato.core.entity.filter.SearchTerms;
import junitparams.Parameters;

/**
 * Test class to integration test the search term and the search term evaluator.
 */

public class BooleanSearchTermEvaluatorIntegrationTest extends SearchTermEvaluatorIntegrationTest<BooleanSearchTerm> {

    @SuppressWarnings("unused")
    private Object[] booleanParameters() {
        return new Object[] {
            // @formatter:off
            new Object[] { "true", true, "fn = true" },
            new Object[] { "false", false, "fn = false" }
            // @formatter:on
        };
    }

    @Override
    protected int getSearchTermType() {
        return SearchTermType.BOOLEAN.getId();
    }

    @Override
    protected BooleanSearchTerm makeSearchTerm(String rawSearchTerm) {
        return (BooleanSearchTerm) SearchTerms.newSearchTerm(ID, searchTermType, SC_ID, FN, rawSearchTerm);
    }

    @Override
    protected BooleanSearchTermEvaluator getEvaluator() {
        return new BooleanSearchTermEvaluator();
    }

    @Test
    @Parameters(method = "booleanParameters")
    public void booleanTest(String rawSearchTerm, Boolean value, String condition) {
        final BooleanSearchTerm st = makeSearchTerm(rawSearchTerm);
        assertThat(st.getValue()).isEqualTo(value);

        final BooleanSearchTermEvaluator ste = getEvaluator();
        final Condition s = ste.evaluate(st);

        assertThat(s.toString()).isEqualTo(condition);
    }

}
