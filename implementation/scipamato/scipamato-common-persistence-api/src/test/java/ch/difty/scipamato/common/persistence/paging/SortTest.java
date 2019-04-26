package ch.difty.scipamato.common.persistence.paging;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.common.persistence.paging.Sort.SortProperty;

class SortTest {

    private final List<SortProperty> sortProperties = new ArrayList<>(4);

    private Sort sort;

    @BeforeEach
    void setUp() {
        sortProperties.add(new SortProperty("a", Direction.ASC));
        sortProperties.add(new SortProperty("b", Direction.DESC));
        sortProperties.add(new SortProperty("c", Direction.DESC));
        sortProperties.add(new SortProperty("d", Direction.ASC));

        sort = new Sort(sortProperties);
    }

    @Test
    void degenerateConstruction_withNullSortProperties_throws() {
        assertDegenerateSupplierParameter(() -> new Sort(null), "sortProperties");
    }

    @Test
    void degenerateConstruction_withNoSortProperties_throws() {
        try {
            new Sort(Collections.emptyList());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("sortProperties can't be empty.");
        }
    }

    @Test
    void degenerateConstruction_withNullPropertyNames_throws() {
        assertDegenerateSupplierParameter(() -> new Sort(Direction.ASC, (String[]) null), "propertyNames");
    }

    @Test
    void degenerateConstruction_withEmptyPropertyNames_throws() {
        try {
            new Sort(Direction.ASC);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("propertyNames can't be empty.");
        }
    }

    private void assertSortProperty(Direction dir, String[] propertyNames) {
        final Sort sort = new Sort(dir, propertyNames);

        assertThat(sort.iterator()).hasSize(propertyNames.length);

        for (final SortProperty sp : sort)
            assertThat(sp.getDirection()).isEqualTo(dir);
    }

    @Test
    void creatingSortForThreeAscendingProperties_returnsIteratorForAllThreePropertiesInAscendingOrder() {
        assertSortProperty(Direction.ASC, new String[] { "a", "b", "c" });
    }

    @Test
    void cratingSortForFiveDescendingProperties_returnsIteratorForAllFiveElementsWithDescendingOrder() {
        assertSortProperty(Direction.DESC, new String[] { "a", "b", "c", "d", "e" });
    }

    @Test
    void creatingSortForFourSortPropertiesWithDifferentSortDirections() {
        final Iterator<SortProperty> it = sort.iterator();
        assertSortProperty(it, Direction.ASC, "a");
        assertSortProperty(it, Direction.DESC, "b");
        assertSortProperty(it, Direction.DESC, "c");
        assertSortProperty(it, Direction.ASC, "d");
        assertThat(it.hasNext()).isFalse();
    }

    private void assertSortProperty(Iterator<SortProperty> it, Direction dir, String p) {
        SortProperty sp = it.next();
        assertThat(sp.getDirection()).isEqualTo(dir);
        assertThat(sp.getName()).isEqualTo(p);
    }

    @Test
    void gettingSortPropertyFor_withNullName_returnsNull() {
        assertThat(sort.getSortPropertyFor(null)).isNull();
    }

    @Test
    void gettingSortPropertyFor_nonExistingName_returnsNull() {
        String p = "x";
        assertThat(sortProperties)
            .extracting("name")
            .doesNotContain(p);
        assertThat(sort.getSortPropertyFor(p)).isNull();
    }

    @Test
    void gettingSortPropertyFor_existingName_returnsRespectiveSortProperty() {
        String p = "c";
        assertThat(sortProperties)
            .extracting("name")
            .contains(p);
        assertThat(sort
            .getSortPropertyFor(p)
            .getName()).isEqualTo(p);
    }

    @Test
    void directionAsc_isAscending() {
        assertThat(Direction.ASC.isAscending()).isTrue();
    }

    @Test
    void directionDesc_isNotAscending() {
        assertThat(Direction.DESC.isAscending()).isFalse();
    }

    @Test
    void sortPropertyWithNullDirection_isAscending() {
        assertThat(new SortProperty("foo", null).getDirection()).isEqualTo(Direction.ASC);
    }

    @Test
    void testingToString() {
        assertThat(sort.toString()).isEqualTo("a: ASC,b: DESC,c: DESC,d: ASC");
    }

    @SuppressWarnings({ "unlikely-arg-type", "ConstantConditions", "RedundantStringConstructorCall", "EqualsWithItself",
        "EqualsBetweenInconvertibleTypes" })
    @Test
    void sortEqualityTests() {
        assertThat(sort.equals(null)).isFalse();
        assertThat(sort.equals("")).isFalse();
        assertThat(sort.equals(sort)).isTrue();
        assertThat(sort.equals(new Sort(sortProperties))).isTrue();

        List<SortProperty> sortProperties2 = new ArrayList<>();
        sortProperties2.add(new SortProperty("a", Direction.ASC));
        sortProperties2.add(new SortProperty("b", Direction.DESC));
        sortProperties2.add(new SortProperty("c", Direction.DESC));
        assertThat(sort.equals(new Sort(sortProperties2))).isFalse();
        assertThat(sort.hashCode()).isNotEqualTo(new Sort(sortProperties2).hashCode());

        sortProperties2.add(new SortProperty("d", Direction.ASC));
        assertThat(sort.equals(new Sort(sortProperties2))).isTrue();
        assertThat(sort.hashCode()).isEqualTo(new Sort(sortProperties2).hashCode());
    }

    @SuppressWarnings({ "unlikely-arg-type", "ConstantConditions", "RedundantStringConstructorCall", "EqualsWithItself",
        "EqualsBetweenInconvertibleTypes" })
    @Test
    void sortPropertyEqualityTests() {
        SortProperty sf1 = new SortProperty("foo", Direction.DESC);

        assertThat(sf1.equals(null)).isFalse();
        assertThat(sf1.equals("")).isFalse();
        assertThat(sf1.equals(sf1)).isTrue();
        assertThat(sf1.equals(new SortProperty("foo", Direction.DESC))).isTrue();
        assertThat(sf1.equals(new SortProperty("foo", Direction.ASC))).isFalse();
        assertThat(sf1.equals(new SortProperty("bar", Direction.DESC))).isFalse();

    }
}
