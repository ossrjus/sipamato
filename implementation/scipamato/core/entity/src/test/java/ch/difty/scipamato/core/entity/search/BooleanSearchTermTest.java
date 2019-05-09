package ch.difty.scipamato.core.entity.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BooleanSearchTermTest {

    private static final long   CONDITION_ID = 3;
    private static final String FIELD_NAME   = "fn";

    private BooleanSearchTerm st;

    private void assertTerm(boolean value, String raw) {
        assertThat(st.getSearchTermType()).isEqualTo(SearchTermType.BOOLEAN);
        assertThat(st.getSearchConditionId()).isEqualTo(CONDITION_ID);
        assertThat(st.getFieldName()).isEqualTo(FIELD_NAME);
        assertThat(st.getValue()).isEqualTo(value);
        assertThat(st.getRawSearchTerm()).isEqualTo(raw);
        assertThat(st.getDisplayValue()).isEqualTo((!value ? "-" : "") + FIELD_NAME);
    }

    @Test
    void ifTrue() {
        final String raw = "true";
        st = new BooleanSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(true, raw);
    }

    @Test
    void ifTrue_withSpaces() {
        final String raw = " true   ";
        st = new BooleanSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(true, raw);
    }

    @Test
    void ifFalse() {
        final String raw = "false";
        st = new BooleanSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(false, raw);
    }

    @Test
    void ifFalse_withSpaces() {
        final String raw = " false  ";
        st = new BooleanSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(false, raw);
    }
}
