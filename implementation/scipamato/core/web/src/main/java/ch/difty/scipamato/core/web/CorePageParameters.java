package ch.difty.scipamato.core.web;

public enum CorePageParameters {

    SEARCH_ORDER_ID("searchOrderId"),
    SHOW_EXCLUDED("showExcluded"),
    USER_ID("userId"),
    MODE("mode"),
    TAB_INDEX("tabIndex");

    private final String name;

    CorePageParameters(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
