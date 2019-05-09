package ch.difty.scipamato.core.web.model;

import java.util.List;

import ch.difty.scipamato.common.web.model.CodeClassLikeModel;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.persistence.CodeClassService;

/**
 * Model that offers a wicket page to load {@link CodeClass}es.
 *
 * @author u.joss
 */
public class CodeClassModel extends CodeClassLikeModel<CodeClass, CodeClassService> {

    private static final long serialVersionUID = 1L;

    public CodeClassModel(final String languageCode) {
        super(languageCode);
    }

    /** just delegating to super, but making load visible to test */
    @Override
    protected List<CodeClass> load() {
        return super.load();
    }

}
