package ch.difty.scipamato.core.persistence.user;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.entity.search.UserFilter;
import ch.difty.scipamato.core.persistence.UserRepository;
import ch.difty.scipamato.core.persistence.UserService;

/**
 * jOOQ specific implementation of the {@link UserService} interface.
 * <p>
 * Note: This service is deliberately not extending JooqEntityService as that
 * depending on on this service itself.
 *
 * @author u.joss
 */
@Service
public class JooqUserService implements UserService {

    private final UserRepository repo;

    private final PasswordEncoder passwordEncoder;

    public JooqUserService(final UserRepository repo, @Lazy final PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(repo.findById(id));
    }

    @Override
    public List<User> findPageByFilter(UserFilter filter, PaginationContext paginationContext) {
        return repo.findPageByFilter(filter, paginationContext);
    }

    @Override
    public int countByFilter(UserFilter filter) {
        return repo.countByFilter(filter);
    }

    @Override
    public User saveOrUpdate(User user) {
        if (user == null)
            return null;
        final String password = user.getPassword();
        if (password != null)
            user.setPassword(passwordEncoder.encode(password));
        if (user.getId() == null)
            return repo.add(user);
        else
            return repo.update(user);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        if (userName == null)
            return Optional.empty();
        return Optional.ofNullable(repo.findByUserName(userName));
    }

    @Override
    public void remove(User entity) {
        if (entity != null && entity.getId() != null)
            repo.delete(entity.getId(), entity.getVersion());
    }

    @Override
    public List<Integer> findPageOfIdsByFilter(final UserFilter filter, final PaginationContext paginationContext) {
        return repo.findPageOfIdsByFilter(filter, paginationContext);
    }

}
