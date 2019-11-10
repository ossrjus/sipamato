package ch.difty.scipamato.publ.web.paper.browse;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.web.event.WicketEvent;

/**
 * The event indicates that one of the filter fields has changed that are
 * present multiple times on the SimpleFilterPanel and therefore need
 * synchronization.
 *
 * @author u.joss
 */
@Getter
@EqualsAndHashCode(callSuper = true)
class SimpleFilterPanelChangeEvent extends WicketEvent {

    private String id;
    private String markupId;

    SimpleFilterPanelChangeEvent(@NotNull AjaxRequestTarget target) {
        super(target);
    }

    SimpleFilterPanelChangeEvent withId(@NotNull String id) {
        this.id = id;
        return this;
    }

    SimpleFilterPanelChangeEvent withMarkupId(@NotNull String markupId) {
        this.markupId = markupId;
        return this;
    }

    /**
     * A component is added to the target if it
     * <ul>
     * <li>has the same id but a different markupId than the event source</li>
     * <li>has the same id as the event source but the latter did not specify a
     * markupId</li>
     * <li>the event source has not specified an id</li>
     * </ul>
     *
     * @param component
     *     the candidate to be added to the target
     */
    void considerAddingToTarget(@NotNull final FormComponent<?> component) {
        if (isValidTarget(component.getId(), component.getMarkupId())) {
            getTarget().add(component);
        }
    }

    private boolean isValidTarget(final String id, final String markupId) {
        return this.id == null || (this.id.equals(id) && (this.markupId == null || !this.markupId.equals(markupId)));
    }
}
