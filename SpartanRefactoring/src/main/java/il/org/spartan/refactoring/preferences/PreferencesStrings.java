package il.org.spartan.refactoring.preferences;

/**
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code> (original /
 *         2014/6/21)
 * @author Tomer Zeltzer <code><tomerr90 [at] gmail.com></code> (original /
 *         2014/6/21)
 * @since 2014/6/21 Manages strings for preferences page
 */
public final class PreferencesStrings {
  /**
   * Preferences page strings
   */
  @SuppressWarnings("javadoc") public static class Strings {
    public static final String description = "Select for each type of tranformation suggestion if it will be shown\n\n";
  }
  /**
   * Strings for Shortest Operand First positioning options
   */
  public static final String atEndString = " at End";
  @SuppressWarnings("javadoc") public static final String atStartString = " at Start";
  @SuppressWarnings("javadoc") public static final String Shortest = "By Shortest Length";
  /**
   * Strings for both Literals positioning options
   */
  public static final String bothLiteralsHeader = "Allow Literals Repositioning";
  @SuppressWarnings("javadoc") public static final String repositionLiterals = "Reposition Literals";
  @SuppressWarnings("javadoc") public static final String repositionAllLiterals = repositionLiterals;
  @SuppressWarnings("javadoc") public static final String doNotRepositionLiterals = "Do not " + repositionLiterals;
  @SuppressWarnings("javadoc") public static final String[][] BothLiteralsOptions = { { repositionAllLiterals, repositionAllLiterals },
      { doNotRepositionLiterals, doNotRepositionLiterals } };
  /**
   * Strings for right Literals positioning options
   */
  public static final String repositionRightLiteralsHeader = "Behavior On Right Operand Literals";
  @SuppressWarnings("javadoc") public static final String repositionRightLiterals = "Reposition right literals";
  @SuppressWarnings("javadoc") public static final String repositionRightException = repositionRightLiterals + " except boolean and null (Overrides Null and Boolean option)";
  @SuppressWarnings("javadoc") public static final String doNotRepositionRightLiterals = "Do not reposition right literals";
  @SuppressWarnings("javadoc") public static final String[][] rightLiteralOptions = { { repositionRightLiterals, repositionRightLiterals },
      { repositionRightException, repositionRightException }, { doNotRepositionRightLiterals, doNotRepositionRightLiterals } };
  /**
   * Strings for Null and Boolean positioning options
   */
  public static final String nullAndBoolHeader = "Null & Boolean Positioning";
  @SuppressWarnings("javadoc") public static final String NullAndBool = "Null and Boolean";
  @SuppressWarnings("javadoc") public static final String NullAndBoolAtEnd = NullAndBool + atEndString;
  @SuppressWarnings("javadoc") public static final String NullAndBoolAtStart = NullAndBool + atStartString;
  @SuppressWarnings("javadoc") public static final String NullAndBoolAtNone = Shortest;
  @SuppressWarnings("javadoc") public static final String[][] NullAndBoolOptions = { { NullAndBoolAtEnd, NullAndBoolAtEnd }, { NullAndBoolAtStart, NullAndBoolAtStart },
      { NullAndBoolAtNone, NullAndBoolAtNone } };
  /**
   * Strings for Shortest Operand resolution
   */
  public static final String swapMessageHeader = "Operand Swap Messages";
  @SuppressWarnings("javadoc") public static final String showOneSwap = "One message per expression that contains operand swap";
  @SuppressWarnings("javadoc") public static final String showEverySwap = "Show message for every operand swap";
  @SuppressWarnings("javadoc") public static final String[][] swapMessageOptions = { { showOneSwap, showOneSwap }, { showEverySwap, showEverySwap } };
}
