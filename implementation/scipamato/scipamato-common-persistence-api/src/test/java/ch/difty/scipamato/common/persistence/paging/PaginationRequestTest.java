package ch.difty.scipamato.common.persistence.paging;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.persistence.paging.Sort.Direction;

class PaginationRequestTest {

    private PaginationRequest pr;
    private String            sort;

    @Test
    void degenerateConstruction_withInvalidOffset() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PaginationRequest(-1, 1));
    }

    @Test
    void degenerateConstruction_withInvalidPageSize() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PaginationRequest(0, 0));
    }

    private void assertPaginationRequest(PaginationContext pc, int offSet, int pageSize, String fooSort) {
        assertThat(pc.getOffset()).isEqualTo(offSet);
        assertThat(pc.getPageSize()).isEqualTo(pageSize);
        if (fooSort != null) {
            final Sort s = pc.getSort();
            assertThat(s
                .getSortPropertyFor("foo")
                .toString()).isEqualTo(fooSort);
            assertThat(s.getSortPropertyFor("bar")).isNull();
        } else {
            assertThat(pc.getSort()).isNull();
        }
    }

    @Test
    void paginationContextWithSortButNoPaging() {
        sort = "foo: DESC";
        pr = new PaginationRequest(Direction.DESC, "foo");

        assertPaginationRequest(pr, 0, Integer.MAX_VALUE, sort);
        assertThat(pr.toString()).isEqualTo("Pagination request [offset: 0, size 2147483647, sort: foo: DESC]");
    }

    @Test
    void paginationContextWithSort_withOffset0_andPageSize10() {
        sort = "foo: DESC";
        pr = new PaginationRequest(0, 10, Direction.DESC, "foo");

        assertPaginationRequest(pr, 0, 10, sort);
        assertThat(pr.toString()).isEqualTo("Pagination request [offset: 0, size 10, sort: foo: DESC]");
    }

    @Test
    void paginationContextWithSort_withOffset24_andPageSize12() {
        sort = "foo: ASC";
        pr = new PaginationRequest(24, 12, Direction.ASC, "foo");

        assertPaginationRequest(pr, 24, 12, sort);
        assertThat(pr.toString()).isEqualTo("Pagination request [offset: 24, size 12, sort: foo: ASC]");
    }

    @Test
    void paginationContextWithoutSort_withOffset6_andPageSize2() {
        sort = null;
        pr = new PaginationRequest(6, 2);

        assertPaginationRequest(pr, 6, 2, sort);
        assertThat(pr.toString()).isEqualTo("Pagination request [offset: 6, size 2, sort: null]");
    }

    @Test
    void equality_ofSameObjectInstance() {
        pr = new PaginationRequest(5, 5);
        assertEquality(pr, pr);
    }

    private void assertEquality(PaginationRequest pr1, PaginationRequest pr2) {
        assertThat(pr1.equals(pr2)).isTrue();
        assertThat(pr1.hashCode()).isEqualTo(pr2.hashCode());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void inequality_ofNull() {
        pr = new PaginationRequest(5, 5);
        assertThat(pr.equals(null)).isFalse();
    }

    @SuppressWarnings({ "unlikely-arg-type", "EqualsBetweenInconvertibleTypes" })
    @Test
    void inequality_ofDifferentClass() {
        pr = new PaginationRequest(5, 5);
        assertThat("foo".equals(pr)).isFalse();
    }

    @Test
    void inequality_ofPaginationRequestWithDifferentSorts() {
        pr = new PaginationRequest(5, 5);
        assertInequality(pr, new PaginationRequest(5, 5, Direction.ASC, "foo"));
    }

    private void assertInequality(PaginationRequest pr1, PaginationRequest pr2) {
        assertThat(pr1.equals(pr2)).isFalse();
        assertThat(pr1.hashCode()).isNotEqualTo(pr2.hashCode());
    }

    @Test
    void inequality_ofPaginationRequestWithDifferentSorts2() {
        pr = new PaginationRequest(5, 6, Direction.ASC, "bar");
        assertInequality(pr, new PaginationRequest(5, 6, Direction.ASC, "foo"));
    }

    @Test
    void inequality_ofPaginationRequestWithDifferentSorts3() {
        pr = new PaginationRequest(5, 6, Direction.DESC, "foo");
        assertInequality(pr, new PaginationRequest(5, 6, Direction.ASC, "foo"));
    }

    @Test
    void inequality_ofPaginationRequestWithNonSortAttributes1() {
        pr = new PaginationRequest(5, 6, Direction.ASC, "foo");
        assertInequality(pr, new PaginationRequest(6, 6, Direction.ASC, "foo"));
    }

    @Test
    void inequality_ofPaginationRequestWithNonSortAttributes2() {
        pr = new PaginationRequest(5, 6, Direction.ASC, "foo");
        assertInequality(pr, new PaginationRequest(5, 7, Direction.ASC, "foo"));
    }

    @Test
    void equality_ofPaginationRequestWithSameAttributes_withSort() {
        pr = new PaginationRequest(5, 6, Direction.ASC, "foo");
        assertEquality(pr, new PaginationRequest(5, 6, Direction.ASC, "foo"));
    }

    @Test
    void equality_ofPaginationRequestWithSameAttributes_withoutSort() {
        pr = new PaginationRequest(5, 6);
        PaginationRequest pr2 = new PaginationRequest(5, 6);
        assertEquality(pr, pr2);
    }

    @SuppressWarnings({ "unlikely-arg-type", "RedundantStringConstructorCall", "EqualsBetweenInconvertibleTypes" })
    @Test
    void inequality_ofPaginationRequestWithNonPaginationRequest() {
        pr = new PaginationRequest(5, 6);
        String pr2 = "";
        assertThat(pr.equals(pr2)).isFalse();
        assertThat(pr.hashCode()).isNotEqualTo(pr2.hashCode());
    }

}
