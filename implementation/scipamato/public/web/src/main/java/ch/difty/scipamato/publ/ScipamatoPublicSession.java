package ch.difty.scipamato.publ;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import org.apache.wicket.Session;
import org.apache.wicket.request.Request;

import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.navigator.LongNavigator;

/**
 * Scipamato Public specific Session
 *
 * @author u.joss
 */
@SuppressWarnings("WeakerAccess")
public final class ScipamatoPublicSession extends SecureWebSession {

    private static final long serialVersionUID = 1L;

    private final ItemNavigator<Long> paperIdManager = new LongNavigator();

    public ScipamatoPublicSession(final Request request) {
        super(request);
    }

    public static ScipamatoPublicSession get() {
        return (ScipamatoPublicSession) Session.get();
    }

    public ItemNavigator<Long> getPaperIdManager() {
        return paperIdManager;
    }

}
