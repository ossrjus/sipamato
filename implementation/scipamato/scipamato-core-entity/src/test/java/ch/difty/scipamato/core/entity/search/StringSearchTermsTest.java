package ch.difty.scipamato.core.entity.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

@SuppressWarnings("SameParameterValue")
class StringSearchTermsTest {

    private static final String KEY   = "key";
    private static final String VALUE = "value";

    private final StringSearchTerms st1 = new StringSearchTerms();
    private final StringSearchTerms st2 = new StringSearchTerms();

    @Test
    void compareEmptySearchTerms_withEmptySearchTerms_match() {
        assertEqualityBetween(st1, st2, 1);

        st1.put(KEY, SearchTerm.newStringSearchTerm(KEY, VALUE));
        st2.put(KEY, SearchTerm.newStringSearchTerm(KEY, VALUE));
    }

    @Test
    void compareEmptySearchTerms_withSingleIdenticalKeyValueSearchTerm_match() {
        st1.put(KEY, SearchTerm.newStringSearchTerm(KEY, VALUE));
        st2.put(KEY, SearchTerm.newStringSearchTerm(KEY, VALUE));
        assertEqualityBetween(st1, st2, 118234894);
    }

    @Test
    void compareEmptySearchTerms_withDoubleIdenticalKeyValueSearchTerm_match() {
        st1.put("key1", SearchTerm.newStringSearchTerm("key1", VALUE));
        st2.put("key1", SearchTerm.newStringSearchTerm("key1", VALUE));
        st1.put("key2", SearchTerm.newStringSearchTerm("key2", "value2"));
        st2.put("key2", SearchTerm.newStringSearchTerm("key2", "value2"));
        assertEqualityBetween(st1, st2, 266203500);
    }

    private void assertEqualityBetween(StringSearchTerms st1, StringSearchTerms st2, int hashValue) {
        assertThat(st1.equals(st2)).isTrue();
        assertThat(st2.equals(st1)).isTrue();
        assertThat(st1.hashCode()).isEqualTo(st2.hashCode());
        assertThat(st1.hashCode()).isEqualTo(hashValue);
    }

    @Test
    void compareEmptySearchTerms_withDifferentSearchTerms_dontMatch() {
        st1.put(KEY, SearchTerm.newStringSearchTerm(KEY, VALUE));
        assertInequalityBetween(st1, st2, 118234894, 1);
    }

    private void assertInequalityBetween(StringSearchTerms st1, StringSearchTerms st2, int hashValue1, int hashValue2) {
        assertThat(st1.equals(st2)).isFalse();
        assertThat(st2.equals(st1)).isFalse();
        assertThat(st1.hashCode()).isNotEqualTo(st2.hashCode());
        assertThat(st1.hashCode()).isEqualTo(hashValue1);
        assertThat(st2.hashCode()).isEqualTo(hashValue2);
    }

    @Test
    void compareEmptySearchTerms_withDifferentSearchTermValues_dontMatch() {
        st1.put(KEY, SearchTerm.newStringSearchTerm(KEY, VALUE));
        st2.put(KEY, SearchTerm.newStringSearchTerm(KEY, "valueX"));
        assertInequalityBetween(st1, st2, 118234894, -817550684);
    }

    @SuppressWarnings({ "unlikely-arg-type", "EqualsWithItself", "ConstantConditions",
        "EqualsBetweenInconvertibleTypes" })
    @Test
    void compareWithNullSelfOrDifferentClass() {
        assertThat(st1.equals(null)).isFalse();
        assertThat(st1.equals(st1)).isTrue();
        assertThat(st1.equals("")).isFalse();
    }

}
