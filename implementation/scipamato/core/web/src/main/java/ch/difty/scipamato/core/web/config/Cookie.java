package ch.difty.scipamato.core.web.config;

/**
 * Enum containing all Cookies used within SciPaMaTo. Names must be unique.
 *
 * @author u.joss
 */
@SuppressWarnings("SameParameterValue")
public enum Cookie {
    PAPER_LIST_PAGE_MODAL_WINDOW("xmlPasteModal-1");

    private static final String TAG = "SciPaMaTo-";

    private final String name;

    Cookie(final String name) {
        this.name = TAG + name;
    }

    public String getName() {
        return name;
    }
}
