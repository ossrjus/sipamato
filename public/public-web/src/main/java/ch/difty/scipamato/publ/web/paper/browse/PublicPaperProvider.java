package ch.difty.scipamato.publ.web.paper.browse;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.publ.persistence.api.PublicPaperService;

/**
 * The data provider for {@link PublicPaper} entities providing the wicket
 * components access to the persisted data
 *
 * @author u.joss
 */
class PublicPaperProvider extends SortableDataProvider<PublicPaper, String>
    implements ISortableDataProvider<PublicPaper, String>, IFilterStateLocator<PublicPaperFilter> {

    private static final long serialVersionUID = 1L;

    private       PublicPaperFilter paperFilter;
    private final int               maxRowsPerPage;

    @SpringBean
    private PublicPaperService service;

    PublicPaperProvider(@Nullable final PublicPaperFilter paperFilter, final int resultPageSize) {
        Injector
            .get()
            .inject(this);
        this.paperFilter = paperFilter != null ? paperFilter : new PublicPaperFilter();
        this.maxRowsPerPage = resultPageSize;
        setSort(PublicPaper.PublicPaperFields.NUMBER.getFieldName(), SortOrder.DESCENDING);
    }

    /** package-private for test purposes */
    void setService(@NotNull final PublicPaperService service) {
        this.service = service;
    }

    /**
     * Return the (max) rowsPerPage (or pageSize), regardless of the number of
     * records actually available on the page.
     *
     * @return rows per page
     */
    int getRowsPerPage() {
        return maxRowsPerPage;
    }

    @Override
    public Iterator<? extends PublicPaper> iterator(long offset, long count) {
        Direction dir = getSort().isAscending() ? Direction.ASC : Direction.DESC;
        String sortProp = getSort().getProperty();
        PaginationContext pc = new PaginationRequest((int) offset, (int) count, dir, sortProp);
        return service
            .findPageByFilter(paperFilter, pc)
            .iterator();
    }

    @Override
    public long size() {
        return service.countByFilter(paperFilter);
    }

    @NotNull
    @Override
    public IModel<PublicPaper> model(@Nullable PublicPaper entity) {
        return new Model<>(entity);
    }

    @Nullable
    @Override
    public PublicPaperFilter getFilterState() {
        return paperFilter;
    }

    @Override
    public void setFilterState(@Nullable final PublicPaperFilter filterState) {
        this.paperFilter = filterState;
    }

    /**
     * Applies the normal filter and the sort aspect of the pageable to return only
     * the numbers (business key) of all papers (un-paged).
     *
     * @return list of all paper numbers
     */
    @NotNull
    List<Long> findAllPaperNumbersByFilter() {
        final Direction dir = getSort().isAscending() ? Direction.ASC : Direction.DESC;
        final String sortProp = getSort().getProperty();
        return service.findPageOfNumbersByFilter(getFilterState(), new PaginationRequest(dir, sortProp));
    }
}
