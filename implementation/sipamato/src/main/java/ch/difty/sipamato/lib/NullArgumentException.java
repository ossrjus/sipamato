package ch.difty.sipamato.lib;

public class NullArgumentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NullArgumentException(final String argName) {
        super((argName == null ? "Argument" : argName) + " must not be null.");
    }

}
