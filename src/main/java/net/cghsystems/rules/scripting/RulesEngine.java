package net.cghsystems.rules.scripting;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * A delegate for {@link ScriptEngineManager} that provides emthods to evaluate
 * a rule defined in any JSR-223 compliant scripting language.
 */
public class RulesEngine {

    /**
     * Can be any JSR-223 compliant script engine Groovy, Python, Jelly, Ruby
     * etc. (Javascript used as the Rhino engine is supplied as a feature of
     * Java 1.6+ and requires no extra third party dependencies)
     */
    public static final String SCRIPT_ENGINE_NAME = "JavaScript";

    private ScriptEngine scriptEngine;

    private RulesEngine() {
        ScriptEngineManager manager = new ScriptEngineManager();
        scriptEngine = manager.getEngineByName(SCRIPT_ENGINE_NAME);
    }

    /**
     * Delegate to {@link ScriptEngine#eval(String)}
     * 
     * @param The
     *            script language source to be executed.
     * @return The value returned from the execution of the script.
     * @throws ScriptException
     */
    public Object evalute(String rule) throws ScriptException {
        return scriptEngine.eval(rule);
    }

    /**
     * @param The
     *            script language source to be executed.
     * @param argument
     *            that can be used in the execution of the script language
     *            source.
     * @return The value returned from the execution of the script.
     * @throws ScriptException
     */
    public Object evalute(String rule, Object argument) throws ScriptException {
        // Can also use Bindings implementation to pass arguments directly in to
        // eval method
        scriptEngine.put("variable", argument);
        return evalute(rule);
    }

    /**
     * @param The
     *            script language source to be executed.
     * @param functionName
     *            . The name of the function embedded in the script language
     *            source to be executed.
     * @return The value returned from the execution of the script.
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    public Object evaluateFunction(String rule, String functionName)
            throws ScriptException, NoSuchMethodException {
        return evaluateFunction(rule, functionName, new Object[] {});
    }

    /**
     * @param The
     *            script language source to be executed.
     * @param functionName
     *            . The name of the function embedded in the script language
     *            source to be executed.
     * @param arguments
     *            required to execute functionName.
     * 
     * @return The value returned from the execution of the script.
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    public Object evaluateFunction(String rule, String functionName,
            Object... arguments) throws ScriptException, NoSuchMethodException {
        evalute(rule);
        Invocable invocable = (Invocable) scriptEngine;
        return invocable.invokeFunction(functionName, arguments);
    }

    /**
     * Returns an implementation of an {@link Action} interface using method
     * implementation from functions compiled from the rule.
     * <p>
     * 
     * @param rule
     *            to take function from.
     * @return An implementation of Action interface with method implementation
     *         taken from the provided rule.
     * @throws ScriptException
     */
    public Action getActionInterface(String rule) throws ScriptException {
        evalute(rule);
        Invocable invocable = (Invocable) scriptEngine;
        return invocable.getInterface(Action.class);
    }
}