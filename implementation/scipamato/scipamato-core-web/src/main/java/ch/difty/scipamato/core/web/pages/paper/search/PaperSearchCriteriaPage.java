package ch.difty.scipamato.core.web.pages.paper.search;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.SearchOrder;
import ch.difty.scipamato.core.entity.filter.SearchCondition;
import ch.difty.scipamato.core.persistence.SearchOrderService;
import ch.difty.scipamato.core.web.PageParameterNames;
import ch.difty.scipamato.core.web.pages.BasePage;
import ch.difty.scipamato.core.web.panel.paper.SearchablePaperPanel;

/**
 * Lookalike of the PaperEditPage that works with a {@link SearchCondition} instead of a Paper entity,
 * which can be used as a kind of Query by example (QBE) functionality.
 *
 * The page is instantiated with a model of a {@link SearchCondition} capturing the specification from this form.
 * If instantiated with a {@link SearchOrder} as parameter, it will add the current query
 * specification {@link SearchCondition} to the search order.
 *
 * Submitting the page will call the {@link PaperSearchPage} handing over the updated {@link SearchOrder}.
 *
 * @author u.joss
 */
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
public class PaperSearchCriteriaPage extends BasePage<SearchCondition> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private SearchOrderService searchOrderService;

    public PaperSearchCriteriaPage(final IModel<SearchCondition> searchConditionModel, final long searchOrderId) {
        super(searchConditionModel);
        getPageParameters().add(PageParameterNames.SEARCH_ORDER_ID, searchOrderId);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(makeSearchablePanel("contentPanel"));
    }

    private SearchablePaperPanel makeSearchablePanel(String id) {
        return new SearchablePaperPanel(id, getModel()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onFormSubmit() {
                searchOrderService.saveOrUpdateSearchCondition(getModelObject(), getSearchOrderId(), getLanguageCode());
                setResponsePage(new PaperSearchPage(getPageParameters()));
            }
        };
    }

    private Long getSearchOrderId() {
        final StringValue sv = getPageParameters().get(PageParameterNames.SEARCH_ORDER_ID);
        return sv.isNull() ? null : sv.toLong();
    }

}