package net.cghsystems.rules.scripting;

/**
 * Very simple command object implementation that helps demonstrate the return
 * capabilities of the Java script engine API (JSR-223).
 */
public class DefaultAction implements Action {

    public static final String DEFAULT_RESULT = "expectedDefaultResult";

    /*
     * (non-Javadoc)
     * 
     * @see net.cghsystems.rules.scripting.Action#execute()
     */
    @Override
    public String execute() {
        return DEFAULT_RESULT;
    }
}
