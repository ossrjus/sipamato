package ch.difty.scipamato.core.web.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.springframework.dao.DuplicateKeyException;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.entity.DefinitionEntity;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

@Slf4j
@SuppressWarnings({ "SameParameterValue", "WicketForgeJavaIdInspection" })
public abstract class DefinitionEditPage<E extends DefinitionEntity> extends BasePage<E> {

    private final PageReference callingPageRef;

    private Form<E> form;

    public DefinitionEditPage(final IModel<E> model, final PageReference callingPageRef) {
        super(AssertAs.INSTANCE.notNull(model, "model"));
        this.callingPageRef = callingPageRef;
    }

    protected PageReference getCallingPageRef() {
        return callingPageRef;
    }

    protected Form<E> getForm() {
        return form;
    }

    @Override
    protected final void onInitialize() {
        super.onInitialize();
        queueForm("form");
    }

    private void queueForm(final String id) {
        queue(form = newForm(id));

        queue(new Label("headerLabel", new StringResourceModel("header" + LABEL_RESOURCE_TAG, this, null)));
        queue(newDefinitionHeaderPanel("headerPanel"));

        queue(new Label("translationsLabel", new StringResourceModel("translations" + LABEL_RESOURCE_TAG, this, null)));
        queue(newDefinitionTranslationPanel("translationsPanel"));
    }

    private Form<E> newForm(final String id) {
        return new Form<>(id, new CompoundPropertyModel<>(getModel())) {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                try {
                    E persisted = persistModel();
                    if (persisted != null) {
                        setModelObject(persisted);
                        info(new StringResourceModel("save.successful.hint", this, null)
                            .setParameters(getModelObject().getNullSafeId(), getModelObject().getTranslationsAsString())
                            .getString());
                    } else {
                        handleRepoException();
                    }
                } catch (OptimisticLockingException ole) {
                    handleOptimisticLockingException(ole);
                } catch (DuplicateKeyException dke) {
                    handleDuplicateKeyException(dke);
                } catch (Exception ex) {
                    handleOtherException(ex);
                }
            }

            private void handleRepoException() {
                error(new StringResourceModel("save.unsuccessful.hint", this, null)
                    .setParameters(getModelObject().getNullSafeId(), "")
                    .getString());
            }

            private void handleOptimisticLockingException(final OptimisticLockingException ole) {
                final String msg = new StringResourceModel("save.optimisticlockexception.hint", this, null)
                    .setParameters(ole.getTableName(), getModelObject().getNullSafeId())
                    .getString();
                log.error(msg);
                error(msg);
            }

            private void handleOtherException(final Exception oe) {
                final String msg = new StringResourceModel("save.error.hint", this, null)
                    .setParameters(getModelObject().getNullSafeId(), oe.getMessage())
                    .getString();
                log.error(msg);
                error(msg);
            }
        };
    }

    protected abstract E persistModel();

    protected abstract DefinitionEditHeaderPanel newDefinitionHeaderPanel(final String id);

    protected abstract DefinitionEditTranslationPanel newDefinitionTranslationPanel(final String id);

    protected abstract void handleDuplicateKeyException(final DuplicateKeyException dke);

}
