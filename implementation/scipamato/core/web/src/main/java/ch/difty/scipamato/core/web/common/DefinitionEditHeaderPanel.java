package ch.difty.scipamato.core.web.common;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import ch.difty.scipamato.common.entity.DefinitionEntity;
import ch.difty.scipamato.common.entity.DefinitionTranslation;

@SuppressWarnings("SameParameterValue")
@Slf4j
public abstract class DefinitionEditHeaderPanel<E extends DefinitionEntity<ID, T>, T extends DefinitionTranslation, ID>
    extends BasePanel<E> {

    protected DefinitionEditHeaderPanel(final String id, final IModel<E> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        makeAndQueueFilterFields();
        makeAndQueueActionButtons();
    }

    protected abstract void makeAndQueueFilterFields();

    private void makeAndQueueActionButtons() {
        queue(newSubmitButton("submit"));
        queue(newBackButton("back"));
    }

    private BootstrapButton newSubmitButton(final String id) {
        return new BootstrapButton(id, new StringResourceModel(id + LABEL_RESOURCE_TAG), Buttons.Type.Primary);
    }

    private BootstrapButton newBackButton(final String id) {
        BootstrapButton back = new BootstrapButton(id, new StringResourceModel(id + LABEL_RESOURCE_TAG),
            Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                if (getCallingPageRef() != null)
                    setResponsePage(getCallingPageRef().getPage());
                else
                    setResponsePage(staticResponsePage());
            }
        };
        back.setDefaultFormProcessing(false);
        return back;
    }

    protected abstract PageReference getCallingPageRef();

    protected abstract Class<? extends Page> staticResponsePage();

}
