package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

/**
 * This class defines methods that will represent generic structures of code
 * such as: statements, conditions, blocks, variable declaration and more.
 *
 * @author Oren Afek
 * @since 06-01-2017
 */
@SuppressWarnings({"SameReturnValue", "UnusedReturnValue"})
public class GenericPsiElementStub {

    /**
     * method stub representing a boolean expression for leonidas tippers
     *
     * @param id the serial no to distinct between several boolean expressions in the same tipper
     * @return true
     */
    public static boolean booleanExpression(int id) {
        return booleanExpression();
    }

    public static boolean booleanExpression() {
        return true;
    }

    /**
     * method stub representing a statement for leonidas tippers
     *
     * @param id the serial no to distinct between several statements in the same tipper
     */
    public static Object statement(int id) {
        return new Object();
    }

    public static Object statement() {
        return new Object();
    }

    /**
     * Method stub representing a method for leonidas tippers
     *
     * @param id the serial no to distinct between several statements in the same tipper
     * @return Method object that allows putting constraints on the method
     */
    public static Method method(int id) {
        return new Method("");
    }

    /**
     * Method stub representing a method for leonidas tippers
     *
     * @return Method object that allows putting constraints on the method
     */
    public static Method method() {
        return new Method("");
    }

    /**
     * method stub representing an identifier for leonidas tippers
     *
     * @param id the serial no to distinct between several identifiers in the same tipper
     * @return stub object
     */
    public static Object identifier(int id) {
        return identifier();
    }

    public static Object identifier() {
        return new Object();
    }

    /**
     * method stub representing any code block
     *
     * @param id the serial no to distinct between several identifiers in the same tipper
     */
    public static void anyBlock(int id) {
    }

    public static void anyBlock() {/**/}

    /**
     * method stub representing an array identifier for leonidas tippers
     *
     * @param id the serial no to distinct between several array identifiers in the same tipper
     * @return stub array
     */
    public static Object[] arrayIdentifier(int id) {
        return arrayIdentifier();
    }

    public static Object[] arrayIdentifier() {
        return new Object[0];
    }

    /**
     * Method stub representing a boolean literal, i.e. <code>true</code> or <code>false</code>
     *
     * @param id the serial number to distinguish between several boolean literals in the same
     *           tipper
     * @return stub object (no real use)
     */
    public static Object booleanLiteral(int id) {
        return booleanLiteral();
    }

    /**
     * Method stub representing a boolean literal, i.e. <code>true</code> or <code>false</code>
     *
     * @return stub object (no real use)
     */
    public static Object booleanLiteral() {
        return new Object();
    }

    /**
     * Method stub representing an optional element
     *
     * @param o - the optional element
     * @return stub object (no real use)
     */
    public static Object optional(Object o) {
        return new Object();
    }
    /**
     * An enum representing the different coding blocks.
     */
}
