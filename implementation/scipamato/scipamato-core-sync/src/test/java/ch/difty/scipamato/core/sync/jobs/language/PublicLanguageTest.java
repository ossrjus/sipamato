package ch.difty.scipamato.core.sync.jobs.language;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.core.sync.jobs.PublicEntityTest;

public class PublicLanguageTest extends PublicEntityTest {

    @Test
    public void canSetGet_withStandardFieldsPopulated() {
        PublicLanguage pp = PublicLanguage
            .builder()
            .code("en")
            .lastSynched(SYNCHED)
            .build();

        assertThat(pp.getCode()).isEqualTo("en");
        assertThat(pp.getLastSynched()).isEqualTo(SYNCHED);
    }
}