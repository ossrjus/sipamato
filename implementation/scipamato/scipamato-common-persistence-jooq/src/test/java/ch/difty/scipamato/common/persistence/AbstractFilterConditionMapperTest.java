package ch.difty.scipamato.common.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.jooq.Condition;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;

class AbstractFilterConditionMapperTest {

    private boolean filterCalled = false;

    private static class DummyFilter extends ScipamatoFilter {
        private static final long serialVersionUID = 1L;
    }

    private final AbstractFilterConditionMapper<DummyFilter> mapper = new AbstractFilterConditionMapper<>() {
        @Override
        protected void map(DummyFilter filter, List<Condition> conditions) {
            filterCalled = true;
        }
    };

    @Test
    void mappingWithNullFilter_doesNotMap() {
        assertThat(mapper
            .map(null)
            .toString()).isEqualTo("1 = 1");
        assertThat(filterCalled).isFalse();
    }

    @Test
    void mappingWithNonNullFilter_doesMap() {
        mapper.map(new DummyFilter());
        assertThat(filterCalled).isTrue();
    }

}
