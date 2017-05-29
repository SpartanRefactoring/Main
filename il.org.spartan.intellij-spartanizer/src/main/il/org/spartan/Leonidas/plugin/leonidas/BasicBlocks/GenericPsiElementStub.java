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
    public static boolean booleanExpression(int id, String name) {
        return true;
    }

    public static boolean booleanExpression(int id) {
        return true;
    }

    /**
     * method stub representing a expression for leonidas tippers
     *
     * @param id the serial no to distinct between several boolean expressions in the same tipper
     * @return true
     */
    public static Object expression(int id, String name) {
        return true;
    }

    public static Object expression(int id) {
        return true;
    }



    /**
     * method stub representing a statement for leonidas tippers
     *
     * @param id the serial no to distinct between several statements in the same tipper
     */
    public static Object statement(int id, String name) {
        return new Object();
    }

    public static Object statement(int id) {
        return new Object();
    }


    /**
     * Method stub representing a union of other blocks for leonidas tippers. Example usage:
     * <p>
     * <code>
     * union(1, method(2), statement(3));
     * </code>
     * <p>
     * The above code example represents a union of a method and a statement.
     *
     * @param id   the serial no to distinct between several statements in the same tipper
     * @param objects list of objects this union represents
     * @return Union object that allows putting constraints on the union
     */
    public static Union union(int id, Object... objects) {
        return new Union();
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

    public static Object anyNumberOf(Object o) {
        return new Object();
    }
}
