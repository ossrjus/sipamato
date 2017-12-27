package ch.difty.scipamato.public_.persistence.codeclass;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.codeclass.JooqCodeClassLikeService;
import ch.difty.scipamato.public_.entity.CodeClass;
import ch.difty.scipamato.public_.persistence.api.CodeClassService;

/**
 * jOOQ specific implementation of the {@link CodeClassService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqCodeClassService extends JooqCodeClassLikeService<CodeClass, CodeClassRepository>
        implements CodeClassService {

    private static final long serialVersionUID = 1L;

    public JooqCodeClassService(final CodeClassRepository codeClassRepository) {
        super(codeClassRepository);
    }

}
