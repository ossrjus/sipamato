package ch.difty.scipamato.common.web.model;

import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.entity.CodeClassLike;
import ch.difty.scipamato.common.persistence.CodeClassLikeService;

/**
 * Model used in core/public wicket pages to load {@link CodeClassLike} code
 * class implementations
 *
 * @author u.joss
 *
 * @param <T>
 *            code class implementations of {@link CodeClassLike}
 * @param <S>
 *            service implementations of {@link CodeClassLikeService}
 */
public abstract class CodeClassLikeModel<T extends CodeClassLike, S extends CodeClassLikeService<T>>
        extends InjectedLoadableDetachableModel<T> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private S service;

    private final String languageCode;

    public CodeClassLikeModel(final String languageCode) {
        super();
        this.languageCode = AssertAs.notNull(languageCode, "languageCode");
    }

    /**
     * Protected constructor for testing without wicket application.
     */
    protected CodeClassLikeModel(final String languageCode, final S service) {
        this(languageCode);
        this.service = service;
    }

    @Override
    protected List<T> load() {
        return service.find(languageCode);
    }

}
