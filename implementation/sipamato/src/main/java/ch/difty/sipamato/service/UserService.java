package ch.difty.sipamato.service;

import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.persistance.jooq.user.UserFilter;

/**
 * The {@link UserService} interface - defining {@link User} specific service methods.
 *
 * @author u.joss
 */
public interface UserService extends EntityService<Integer, User, UserFilter> {

}
