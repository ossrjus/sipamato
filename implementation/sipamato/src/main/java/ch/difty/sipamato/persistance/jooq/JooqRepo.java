package ch.difty.sipamato.persistance.jooq;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SortField;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;
import org.slf4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.entity.SipamatoFilter;
import ch.difty.sipamato.lib.Asserts;

/**
 * The generic jOOQ repository.
 *
 * @author u.joss
 *
 * @param <R> the record, extending {@link Record}
 * @param <T> the entity type, extending {@link SipamatoEntity}
 * @param <ID> the id of entity <literal>T</literal>
 * @param <TI> the table implementation of record <literal>R</literal>
 * @param <M> the record mapper, mapping record <literal>R</literal> into entity <literal>T</literal>
 * @param <F> the filter, extending {@link SipamatoFilter}
 */
@Profile("DB_JOOQ")
@Transactional(readOnly = true)
public abstract class JooqRepo<R extends Record, T extends SipamatoEntity, ID, TI extends TableImpl<R>, M extends RecordMapper<R, T>, F extends SipamatoFilter>
        implements GenericRepository<R, T, ID, M, F> {

    private static final long serialVersionUID = 1L;

    private final DSLContext dsl;
    private final M mapper;
    private final InsertSetStepSetter<R, T> insertSetStepSetter;
    private final UpdateSetStepSetter<R, T> updateSetStepSetter;

    protected JooqRepo(DSLContext dsl, M mapper, InsertSetStepSetter<R, T> insertSetStepSetter, UpdateSetStepSetter<R, T> updateSetStepSetter) {
        Asserts.notNull(dsl, "dsl");
        Asserts.notNull(mapper, "mapper");
        Asserts.notNull(insertSetStepSetter, "insertSetStepSetter");
        Asserts.notNull(updateSetStepSetter, "updateSetStepSetter");
        this.dsl = dsl;
        this.mapper = mapper;
        this.insertSetStepSetter = insertSetStepSetter;
        this.updateSetStepSetter = updateSetStepSetter;
    }

    /** protected for test purposes */
    protected DSLContext getDslContext() {
        return dsl;
    }

    /** protected for test purposes */
    public M getMapper() {
        return mapper;
    }

    public InsertSetStepSetter<R, T> getInsertSetStepSetter() {
        return insertSetStepSetter;
    }

    /** protected for test purposes */
    public UpdateSetStepSetter<R, T> getUpdateSetStepSetter() {
        return updateSetStepSetter;
    }

    /**
     * @return return the Repo specific {@link Logger}
     */
    protected abstract Logger getLogger();

    /**
     * @return the Entity Class <code>T.class</code>
     */
    protected abstract Class<? extends T> getEntityClass();

    /**
     * @return the Record Class <code>R.class</code>
     */
    protected abstract Class<? extends R> getRecordClass();

    /**
     * @return the jOOQ generated table of type <code>T</code>
     */
    protected abstract TI getTable();

    /**
     * @return the jOOQ generated {@link TableField} representing the <code>ID</code>
     */
    protected abstract TableField<R, ID> getTableId();

    /**
     * @param record persisted record that now holds the ID from the database.
     * @return the id of type <code>ID</code>
     */
    protected abstract ID getIdFrom(R record);

    /**
     * @param entity persisted entity that now holds the ID from the database.
     * @return the id of type <code>ID</code>
     */
    protected abstract ID getIdFrom(T entity);

    /**
     * @param filter the filter to translate
     * @return the translated {@link Condition}s
     */
    protected abstract Condition createWhereConditions(F filter);

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public T add(final T entity) {
        Asserts.notNull(entity);

        InsertSetMoreStep<R> step = insertSetStepSetter.setNonKeyFieldsFor(dsl.insertInto(getTable()), entity);
        insertSetStepSetter.considerSettingKeyOf(step, entity);

        R saved = step.returning().fetchOne();
        if (saved != null) {
            getLogger().info("Inserted 1 record: {} with id {}.", getTable().getName(), getIdFrom(saved));
            return mapper.map(saved);
        } else {
            getLogger().warn("Unable to insert {} record", getTable().getName());
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public T delete(final ID id) {
        Asserts.notNull(id, "id");

        final T toBeDeleted = findById(id);
        if (toBeDeleted != null) {
            final int deleteCount = dsl.delete(getTable()).where(getTableId().equal(id)).execute();
            if (deleteCount > 0) {
                getLogger().info("Deleted {} record: {} with id {}.", deleteCount, getTable().getName(), id);
            } else {
                getLogger().error("Unable to delete {} with id {}", getTable().getName(), id);
            }
        }
        return toBeDeleted;
    }

    /** {@inheritDoc} */
    @Override
    public List<T> findAll() {
        return dsl.selectFrom(getTable()).fetchInto(getEntityClass());
    }

    /** {@inheritDoc} */
    @Override
    public T findById(final ID id) {
        Asserts.notNull(id, "id");
        return dsl.selectFrom(getTable()).where(getTableId().equal(id)).fetchOneInto(getEntityClass());
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public T update(final T entity) {
        Asserts.notNull(entity, "entity");
        ID id = getIdFrom(entity);
        Asserts.notNull(id, "entity.id");

        R updated = updateSetStepSetter.setFieldsFor(dsl.update(getTable()), entity).where(getTableId().equal(id)).returning().fetchOne();
        if (updated != null) {
            getLogger().info("Updated 1 record: {} with id {}.", getTable().getName(), id);
            return mapper.map(updated);
        } else {
            getLogger().warn("Unable to update {} record with id {}.", getTable().getName(), id);
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(F filter) {
        return dsl.fetchCount(dsl.selectOne().from(getTable()).where(createWhereConditions(filter)));
    }

    /** {@inheritDoc} */
    @Override
    public Page<T> findByFilter(F filter, Pageable pageable) {
        final List<R> queryResults = dsl.selectFrom(getTable()).where(createWhereConditions(filter)).orderBy(getSortFields(pageable.getSort())).fetchInto(getRecordClass());
        final List<T> entities = queryResults.stream().map(getMapper()::map).collect(Collectors.toList());
        return new PageImpl<>(entities, pageable, (long) countByFilter(filter));
    }

    private Collection<SortField<T>> getSortFields(Sort sortSpecification) {
        Collection<SortField<T>> querySortFields = new ArrayList<>();

        if (sortSpecification == null) {
            return querySortFields;
        }

        Iterator<Sort.Order> specifiedFields = sortSpecification.iterator();

        while (specifiedFields.hasNext()) {
            Sort.Order specifiedField = specifiedFields.next();

            String sortFieldName = specifiedField.getProperty();
            Sort.Direction sortDirection = specifiedField.getDirection();

            TableField<R, T> tableField = getTableField(sortFieldName);
            SortField<T> querySortField = convertTableFieldToSortField(tableField, sortDirection);
            querySortFields.add(querySortField);
        }

        return querySortFields;
    }

    @SuppressWarnings("unchecked")
    private TableField<R, T> getTableField(String sortFieldName) {
        TableField<R, T> sortField = null;
        try {
            Field tableField = getTable().getClass().getField(sortFieldName.toUpperCase());
            sortField = (TableField<R, T>) tableField.get(getTable());
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            String errorMessage = String.format("Could not find table field: {}", sortFieldName);
            throw new InvalidDataAccessApiUsageException(errorMessage, ex);
        }

        return sortField;
    }

    private SortField<T> convertTableFieldToSortField(TableField<R, T> tableField, Sort.Direction sortDirection) {
        return sortDirection == Sort.Direction.ASC ? tableField.asc() : tableField.desc();
    }

}
