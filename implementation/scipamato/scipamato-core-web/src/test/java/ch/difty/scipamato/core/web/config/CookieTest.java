package ch.difty.scipamato.core.web.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class CookieTest {

    @Test
    void assertValues() {
        assertThat(Cookie.values()).contains(Cookie.PAPER_LIST_PAGE_MODAL_WINDOW);
    }

    @Test
    void assertPaperListPageModalWindow() {
        assertThat(Cookie.PAPER_LIST_PAGE_MODAL_WINDOW.getName()).isEqualTo("SciPaMaTo-xmlPasteModal-1");
    }

    @Test
    void assertAllNamesAreUnique() {
        final Set<String> cookies = new HashSet<>();
        for (final Cookie c : Cookie.values())
            cookies.add(c.getName());
        assertThat(cookies).hasSize(Cookie.values().length);
    }
}
