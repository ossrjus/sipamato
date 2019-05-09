package ch.difty.scipamato.core.persistence.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.entity.search.UserFilter;
import ch.difty.scipamato.core.persistence.UserRepository;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "ResultOfMethodCallIgnored", "OptionalGetWithoutIsPresent" })
class JooqUserServiceTest {

    private JooqUserService service;

    @Mock
    private UserRepository    repoMock;
    @Mock
    private PasswordEncoder   passwordEncoderMock;
    @Mock
    private UserFilter        filterMock;
    @Mock
    private PaginationContext paginationContextMock;
    @Mock
    private User              userMock;

    private final List<User> users = new ArrayList<>();

    @BeforeEach
    void setUp() {
        service = new JooqUserService(repoMock, passwordEncoderMock);

        users.add(userMock);
        users.add(userMock);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repoMock, passwordEncoderMock, filterMock, paginationContextMock, userMock);
    }

    @Test
    void findingById_withFoundEntity_returnsOptionalOfIt() {
        Integer id = 7;
        when(repoMock.findById(id)).thenReturn(userMock);

        Optional<User> optUser = service.findById(id);
        assertThat(optUser.isPresent()).isTrue();
        assertThat(optUser.get()).isEqualTo(userMock);

        verify(repoMock).findById(id);
    }

    @Test
    void findingById_withNotFoundEntity_returnsOptionalEmpty() {
        Integer id = 7;
        when(repoMock.findById(id)).thenReturn(null);

        assertThat(service
            .findById(id)
            .isPresent()).isFalse();

        verify(repoMock).findById(id);
    }

    @Test
    void findingByFilter_delegatesToRepo() {
        when(repoMock.findPageByFilter(filterMock, paginationContextMock)).thenReturn(users);
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(users);
        verify(repoMock).findPageByFilter(filterMock, paginationContextMock);
    }

    @Test
    void countingByFilter_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

    @Test
    void savingOrUpdating_withNullUser_simplyReturnsNull() {
        assertThat(service.saveOrUpdate(null)).isNull();
    }

    @Test
    void savingOrUpdating_withUserWithNullId_hasRepoAddTheUser() {
        when(userMock.getId()).thenReturn(null);
        when(repoMock.add(userMock)).thenReturn(userMock);
        assertThat(service.saveOrUpdate(userMock)).isEqualTo(userMock);
        verify(repoMock).add(userMock);
        verify(userMock).getId();
        verify(userMock).getPassword();
        verify(userMock, never()).setPassword(anyString());
    }

    @Test
    void savingOrUpdating_withUserWithNonNullId_hasRepoUpdateTheUser() {
        when(userMock.getId()).thenReturn(17);
        when(repoMock.update(userMock)).thenReturn(userMock);
        assertThat(service.saveOrUpdate(userMock)).isEqualTo(userMock);
        verify(repoMock).update(userMock);
        verify(userMock).getId();
        verify(userMock).getPassword();
        verify(userMock, never()).setPassword(anyString());
    }

    @Test
    void savingOrUpdating_withUserWithNullId_withPassword_hasRepoAddTheUserAfterEncodingThePassword() {
        when(userMock.getId()).thenReturn(null);
        when(userMock.getPassword()).thenReturn("foo");
        when(passwordEncoderMock.encode("foo")).thenReturn("bar");
        when(repoMock.add(userMock)).thenReturn(userMock);

        assertThat(service.saveOrUpdate(userMock)).isEqualTo(userMock);

        verify(repoMock).add(userMock);
        verify(passwordEncoderMock).encode("foo");
        verify(userMock).getId();
        verify(userMock).getPassword();
        verify(userMock).setPassword("bar");
    }

    @Test
    void savingOrUpdating_withUserWithNonNullId_withPassword_hasRepoUpdateTheUserAfterEncodingThePassword() {
        when(userMock.getId()).thenReturn(17);
        when(userMock.getPassword()).thenReturn("foo");
        when(passwordEncoderMock.encode("foo")).thenReturn("bar");
        when(repoMock.update(userMock)).thenReturn(userMock);

        assertThat(service.saveOrUpdate(userMock)).isEqualTo(userMock);

        verify(repoMock).update(userMock);
        verify(passwordEncoderMock).encode("foo");
        verify(userMock).getId();
        verify(userMock).getPassword();
        verify(userMock).setPassword("bar");
    }

    @Test
    void findingByUserName_withNullName_returnsEmptyOptional() {
        assertThat(service
            .findByUserName(null)
            .isPresent()).isFalse();
    }

    @Test
    void findingByUserName_whenFindingUser_delegatesToRepoAndReturnsOptionalOfFoundUser() {
        when(repoMock.findByUserName("foo")).thenReturn(userMock);
        assertThat(service.findByUserName("foo")).isEqualTo(Optional.of(userMock));
        verify(repoMock).findByUserName("foo");
    }

    @Test
    void findingByUserName_whenNotFindingUser_delegatesToRepoAndReturnsOptionalEmpty() {
        when(repoMock.findByUserName("foo")).thenReturn(null);
        assertThat(service.findByUserName("foo")).isEqualTo(Optional.empty());
        verify(repoMock).findByUserName("foo");
    }

    @Test
    void deleting_withNullEntity_doesNothing() {
        service.remove(null);
        verify(repoMock, never()).delete(anyInt(), anyInt());
    }

    @Test
    void deleting_withEntityWithNullId_doesNothing() {
        when(userMock.getId()).thenReturn(null);

        service.remove(userMock);

        verify(userMock).getId();
        verify(repoMock, never()).delete(anyInt(), anyInt());
    }

    @Test
    void deleting_withEntityWithNormalId_delegatesToRepo() {
        when(userMock.getId()).thenReturn(3);
        when(userMock.getVersion()).thenReturn(2);

        service.remove(userMock);

        verify(userMock, times(2)).getId();
        verify(userMock, times(1)).getVersion();
        verify(repoMock, times(1)).delete(3, 2);
    }

    @Test
    void findingPageOfIdsByFilter_delegatesToRepo() {
        when(repoMock.findPageOfIdsByFilter(filterMock, paginationContextMock)).thenReturn(Arrays.asList(3, 8, 5));
        assertThat(service.findPageOfIdsByFilter(filterMock, paginationContextMock)).containsExactly(3, 8, 5);
        verify(repoMock).findPageOfIdsByFilter(filterMock, paginationContextMock);
    }

}
