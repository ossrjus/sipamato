package ch.difty.scipamato.core.persistence;

import org.jetbrains.annotations.NotNull;
import org.jooq.Record;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;

import ch.difty.scipamato.core.entity.CoreEntity;

/**
 * Sets the various fields from the entity into the UpdateSetStep.
 *
 * @param <R>
 *     Record extending {@link Record}
 * @param <T>
 *     Entity extending {@link CoreEntity}
 * @author u.joss
 */
@FunctionalInterface
public interface UpdateSetStepSetter<R extends Record, T extends CoreEntity> {

    /**
     * Sets all fields except the primary key(s) from the entity into the step.
     *
     * @param step
     *     the {@link UpdateSetFirstStep} to set the values into
     * @param entity
     *     the entity to set the values from
     * @return {@link UpdateSetMoreStep} for further usage with jOOQ
     */
    @NotNull
    UpdateSetMoreStep<R> setFieldsFor(@NotNull UpdateSetFirstStep<R> step, @NotNull T entity);
}
