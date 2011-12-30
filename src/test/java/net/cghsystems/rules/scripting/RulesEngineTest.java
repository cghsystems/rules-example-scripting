package net.cghsystems.rules.scripting;

import static java.lang.Boolean.TRUE;

import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class RulesEngineTest {

    @TestedObject
    private RulesEngine unit;

    /**
     * Showcases that a rule can be evaluated
     * <p>
     * Given a rule that returns 1.0 When Evaluate is called Then should return
     * 1.0
     */
    @Test
    public void shouldExecuteJavascriptRule() throws ScriptException {
        String rule = "1";
        Object actual = unit.evaluate(rule);
        Assert.assertEquals(1.0, actual);
    }

    /**
     * Showcases that a rule can be executed and a new 'Java' class returned on
     * its execution
     * <p>
     * Given a rule that returns an {@link DefaultAction} object When Evaluate
     * is called Then should return expected Action object
     */
    @Test
    public void shouldExecuteJavascriptRuleAndReturnAnActionObject()
            throws ScriptException {
        String rule = "importPackage(net.cghsystems.rules.scripting); new DefaultAction()";
        Action actual = (Action) unit.evaluate(rule);
        Assert.assertEquals(DefaultAction.DEFAULT_RESULT, actual.execute());
    }

    /**
     * Showcases logic within a rule that executes depending up on a variable
     * passed to the script engine.
     * <p>
     * Given a rule that returns an Action object when an argument is 'true'
     * When evaluate is called with a valid argument of 'true' Then should
     * return expected Action object
     */
    @Test
    public void shouldExecuteJavascriptRuleWithArguementsAndReturnTheExpectedObject()
            throws ScriptException {
        String rule = "importPackage(net.cghsystems.rules.scripting); if(variable==true) { new DefaultAction() } else { 'error' }";
        Action actual = (Action) unit.evaluate(rule, TRUE);
        Assert.assertEquals(DefaultAction.DEFAULT_RESULT, actual.execute());
    }

    /**
     * Showcases logic within a rule that executes depending up on a variable
     * passed to the script engine.
     * <p>
     * Given a rule that returns an Action object when an argument is 'true'
     * When evaluate is called with an invalid argument of 'wrong' Then should
     * return unexpected object
     */
    @Test
    public void shouldExecuteJavascriptRuleWithArguementsAndReturnUnexpectedObject()
            throws ScriptException {
        String rule = "importPackage(net.cghsystems.rules.scripting); if(variable==true) { new Action() } else { 'error' }";
        String actual = (String) unit.evaluate(rule, "wrong");
        Assert.assertEquals("error", actual);
    }

    /**
     * Showcases that a method can be called from within a rule.
     * <p>
     * Given a rule with an embedded function When evaulateFunction is called
     * with the expected function name then Should return the object returned by
     * the embedded function
     */
    @Test
    public void shouldExecuteAMethodEmbeddedInARule() throws ScriptException,
            NoSuchMethodException {
        String rule = "function testFunction() { return 'congratulations' }";
        String actual = (String) unit.evaluateFunction(rule, "testFunction");
        Assert.assertEquals("congratulations", actual);
    }

    /**
     * Showcases that method embedded in a rule can be called and arguments
     * passed to it.
     * <p>
     * Given a rule with an embedded function When evaulateFunction is called
     * with the expected function name then Should return the object returned by
     * the embedded function
     */
    @Test
    public void shouldExecuteAMethodWithArguemtnsEmbeddedInARule()
            throws ScriptException, NoSuchMethodException {
        String rule = "function testFunction(arg) { return arg }";
        String expectedReturnValue = "an expted return value";
        String actual = (String) unit.evaluateFunction(rule, "testFunction",
                expectedReturnValue);
        Assert.assertEquals(expectedReturnValue, actual);
    }

    /**
     * Showcases that an interface can be implemented on a script rule so it can
     * be passed around in a 'Java' system as an expected type.
     * <p>
     * Given a rule with a function with the same method signature as a
     * {@link Action} interface when GetAction THen Should return {@link Action}
     * implementation ready for execution.
     */
    @Test
    public void shouldGetExpectedTypeWithScriptRuleAsMEthodImplementation()
            throws ScriptException {
        String rule = "function execute() { return 'expectedExecuteResult' }";
        Action action = unit.getActionInterface(rule);
        String actual = action.execute();
        Assert.assertEquals("expectedExecuteResult", actual);
    }
}
