package ch.difty.scipamato.core.web.code;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.entity.code.CodeTranslation;
import ch.difty.scipamato.core.persistence.CodeService;
import ch.difty.scipamato.core.web.common.DefinitionListFilterPanel;
import ch.difty.scipamato.core.web.model.CodeClassModel;

@SuppressWarnings("SameParameterValue")
abstract class CodeListFilterPanel
    extends DefinitionListFilterPanel<CodeDefinition, CodeFilter, CodeService, CodeDefinitionProvider> {

    CodeListFilterPanel(final String id, final CodeDefinitionProvider provider) {
        super(id, provider);
    }

    protected void queueFilterFormFields() {
        queueBootstrapSelectAndLabel("codeClass");

        queueFieldAndLabel(new TextField<String>(CodeDefinition.CodeDefinitionFields.NAME.getFieldName(),
            PropertyModel.of(getFilter(), CodeFilter.CodeFilterFields.NAME_MASK.getFieldName())));

        queueFieldAndLabel(new TextField<String>(CodeTranslation.CodeTranslationFields.COMMENT.getFieldName(),
            PropertyModel.of(getFilter(), CodeFilter.CodeFilterFields.COMMENT_MASK.getFieldName())));

        queueNewCodeButton("newCode");
    }

    private void queueBootstrapSelectAndLabel(final String id) {
        queue(new Label(id + LABEL_TAG, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null)));

        final PropertyModel<CodeClass> model = PropertyModel.of(getFilter(),
            CodeFilter.CodeFilterFields.CODE_CLASS.getFieldName());
        final CodeClassModel choices = new CodeClassModel(getLocale().getLanguage());
        final IChoiceRenderer<CodeClass> choiceRenderer = new ChoiceRenderer<>(
            CodeClass.CoreEntityFields.DISPLAY_VALUE.getFieldName(), CodeClass.IdScipamatoEntityFields.ID.getFieldName());

        final BootstrapSelect<CodeClass> codeClasses = new BootstrapSelect<>(id, model, choices, choiceRenderer);
        codeClasses.add(new AjaxFormComponentUpdatingBehavior("change") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                doOnUpdate(target);
            }
        });
        queue(codeClasses);
    }

    protected abstract void doOnUpdate(final AjaxRequestTarget target);

    private void queueNewCodeButton(final String id) {
        queue(doQueueNewCodeButton(id));
    }

    protected abstract BootstrapAjaxButton doQueueNewCodeButton(final String id);

}