package ch.difty.scipamato.common.persistence.codeclass;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.entity.CodeClassLike;

@ExtendWith(MockitoExtension.class)
class JooqCodeClassLikeServiceTest {

    private static final String LANG_CODE = "en";

    private JooqCodeClassLikeService<CodeClassLike, CodeClassLikeRepository<CodeClassLike>> service;

    private final List<CodeClassLike> codeClasses = new ArrayList<>();

    @Mock
    private CodeClassLike cclMock;

    @Mock
    private CodeClassLikeRepository<CodeClassLike> repoMock;

    @BeforeEach
    void setUp() {
        service = new JooqCodeClassLikeService<>(repoMock) {
        };

        codeClasses.add(cclMock);
        codeClasses.add(cclMock);

    }

    @Test
    void canGetRepo() {
        assertThat(service.getRepo()).isEqualTo(repoMock);
    }

    @Test
    void finding_delegatesToRepo() {
        when(repoMock.find(LANG_CODE)).thenReturn(codeClasses);
        assertThat(service.find(LANG_CODE)).containsExactly(cclMock, cclMock);
        verify(repoMock).find(LANG_CODE);
    }
}
