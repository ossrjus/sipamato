package ch.difty.scipamato.core.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.core.entity.CoreEntity;

@SuppressWarnings("WeakerAccess")
public abstract class RecordMapperTest<R extends Record, E extends CoreEntity> {

    public static final int       VERSION     = 1;
    public static final Timestamp CREATED     = new Timestamp(1469999999999L);
    public static final Integer   CREATED_BY  = 1;
    public static final Timestamp LAST_MOD    = new Timestamp(1479999999999L);
    public static final Integer   LAST_MOD_BY = 2;

    private final RecordMapper<R, E> mapper = getMapper();

    protected abstract RecordMapper<R, E> getMapper();

    /**
     * Test fixture for the entity mock audit fields.
     *
     * @param entityMock
     *     the mocked entity
     */
    protected static void auditFixtureFor(CoreEntity entityMock) {
        when(entityMock.getCreatedBy()).thenReturn(CREATED_BY);
        when(entityMock.getLastModifiedBy()).thenReturn(LAST_MOD_BY);
    }

    /**
     * Test fixture for the entity mock audit fields.
     *
     * @param entityMock
     *     the mocked entity
     */
    protected static void auditExtendedFixtureFor(CoreEntity entityMock) {
        when(entityMock.getCreated()).thenReturn(CREATED.toLocalDateTime());
        when(entityMock.getLastModified()).thenReturn(LAST_MOD.toLocalDateTime());
        when(entityMock.getVersion()).thenReturn(VERSION);
    }

    @Test
    void mappingWithNullEntity_throws() {
        Assertions.assertThrows(NullArgumentException.class, () -> mapper.map(null));
    }

    @Test
    void mapping_mapsRecordToEntity() {
        R record = makeRecord();
        setAuditFieldsIn(record);
        E entity = mapper.map(record);
        assertEntity(entity);
        assertAuditFieldsOf(entity);
    }

    /**
     * Create the record and set its field (except for the audit fields, which are
     * set separately).
     */
    protected abstract R makeRecord();

    /**
     * <code><pre>
     *  record.setCreated(CREATED);
     *  record.setCreatedBy(CREATED_BY);
     *  record.setLastModified(LAST_MOD);
     *  record.setLastModifiedBy(LAST_MOD_BY);
     *  record.setVersion(VERSION);
     *  </pre></code>
     *
     * @param record
     *     for which the audit fields are set into
     */
    protected abstract void setAuditFieldsIn(R record);

    /**
     * Assert non-audit fields of entity (audit fields are asserted separately)
     *
     * @param entity
     *     the entity to assert the non-audit fields for
     */
    protected abstract void assertEntity(E entity);

    private void assertAuditFieldsOf(E e) {
        assertThat(e.getVersion()).isEqualTo(VERSION);
        assertThat(e.getCreated()).isEqualTo(CREATED.toLocalDateTime());
        assertThat(e.getCreatedBy()).isEqualTo(CREATED_BY);
        assertThat(e.getLastModified()).isEqualTo(LAST_MOD.toLocalDateTime());
        assertThat(e.getLastModifiedBy()).isEqualTo(LAST_MOD_BY);

        // not enriched by service
        assertThat(e.getCreatedByName()).isNull();
        assertThat(e.getCreatedByFullName()).isNull();
        assertThat(e.getLastModifiedByName()).isNull();
    }

}
