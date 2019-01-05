package ch.difty.scipamato.core.persistence.user;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.entity.search.UserFilter;
import ch.difty.scipamato.core.persistence.EntityRepository;
import ch.difty.scipamato.core.persistence.JooqEntityRepoTest;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class JooqUserRepoTest extends
    JooqEntityRepoTest<ScipamatoUserRecord, User, Integer, ch.difty.scipamato.core.db.tables.ScipamatoUser, UserRecordMapper, UserFilter> {

    private static final Integer SAMPLE_ID = 3;

    private JooqUserRepo repo;

    @Mock
    private UserRoleRepository userRoleRepoMock;

    @Override
    protected Integer getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected JooqUserRepo getRepo() {
        if (repo == null) {
            repo = new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(),
                userRoleRepoMock);
        }
        return repo;
    }

    @Override
    protected EntityRepository<User, Integer, UserFilter> makeRepoFindingEntityById(User user) {
        return new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
            getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(),
            userRoleRepoMock) {
            @Override
            public User findById(Integer id, int version) {
                return user;
            }
        };
    }

    @Mock
    private User unpersistedEntity, persistedEntity;

    @Override
    protected User getUnpersistedEntity() {
        return unpersistedEntity;
    }

    @Override
    protected User getPersistedEntity() {
        return persistedEntity;
    }

    @Mock
    private ScipamatoUserRecord persistedRecord;

    @Override
    protected ScipamatoUserRecord getPersistedRecord() {
        return persistedRecord;
    }

    @Mock
    private UserRecordMapper mapperMock;

    @Override
    protected UserRecordMapper getMapper() {
        return mapperMock;
    }

    @Override
    protected ch.difty.scipamato.core.db.tables.ScipamatoUser getTable() {
        return SCIPAMATO_USER;
    }

    @Override
    protected TableField<ScipamatoUserRecord, Integer> getTableId() {
        return SCIPAMATO_USER.ID;
    }

    @Override
    protected TableField<ScipamatoUserRecord, Integer> getRecordVersion() {
        return SCIPAMATO_USER.VERSION;
    }

    @Override
    protected EntityRepository<User, Integer, UserFilter> makeRepoSavingReturning(final ScipamatoUserRecord returning) {
        return new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
            getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(),
            userRoleRepoMock) {
            @Override
            protected ScipamatoUserRecord doSave(final User entity, final String languageCode) {
                return returning;
            }
        };
    }

    @Mock
    private UserFilter filterMock;

    @Override
    protected UserFilter getFilter() {
        return filterMock;
    }

    @Override
    protected void expectEntityIdsWithValues() {
        when(unpersistedEntity.getId()).thenReturn(SAMPLE_ID);
        when(persistedRecord.getId()).thenReturn(SAMPLE_ID);
    }

    @Override
    protected void expectUnpersistedEntityIdNull() {
        when(unpersistedEntity.getId()).thenReturn(null);
    }

    @Override
    protected void verifyUnpersistedEntityId() {
        verify(getUnpersistedEntity()).getId();
    }

    @Override
    protected void verifyPersistedRecordId() {
        verify(persistedRecord).getId();
    }

    @Test
    public void degenerateConstruction() {
        assertDegenerateSupplierParameter(
            () -> new JooqUserRepo(null, getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(),
                getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(), userRoleRepoMock),
            "dsl");
        assertDegenerateSupplierParameter(
            () -> new JooqUserRepo(getDsl(), null, getSortMapper(), getFilterConditionMapper(), getDateTimeService(),
                getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(), userRoleRepoMock),
            "mapper");
        assertDegenerateSupplierParameter(
            () -> new JooqUserRepo(getDsl(), getMapper(), null, getFilterConditionMapper(), getDateTimeService(),
                getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(), userRoleRepoMock),
            "sortMapper");
        assertDegenerateSupplierParameter(
            () -> new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), null, getDateTimeService(),
                getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(), userRoleRepoMock),
            "filterConditionMapper");
        assertDegenerateSupplierParameter(
            () -> new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), null,
                getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(), userRoleRepoMock),
            "dateTimeService");
        assertDegenerateSupplierParameter(
            () -> new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), null, getUpdateSetStepSetter(), getApplicationProperties(), userRoleRepoMock),
            "insertSetStepSetter");
        assertDegenerateSupplierParameter(
            () -> new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), null, getApplicationProperties(), userRoleRepoMock),
            "updateSetStepSetter");
        assertDegenerateSupplierParameter(
            () -> new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), null, userRoleRepoMock),
            "applicationProperties");
        assertDegenerateSupplierParameter(
            () -> new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(),
                null), "userRoleRepo");
    }

}
