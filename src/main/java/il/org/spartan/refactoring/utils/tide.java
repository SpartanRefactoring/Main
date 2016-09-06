package il.org.spartan.refactoring.utils;

/** Pun intended: <code>tide.WHITES</code>, <code>tide.clean(s)</code>, cleans
 * all <code>tide.WHITES</code> */
public interface tide {
  String WHITES = "(?m)\\s+";

  /** Remove all non-essential spaces from a string that represents Java code.
   * @param javaCodeFragment JD
   * @return parameter, with all redundant spaces removes from it */
  static String clean(final String javaCodeFragment) {
    String $ = javaCodeFragment//
        .replaceAll("(?m)\\s+", " ") // Squeeze whites
        .replaceAll("^\\s", "") // Opening whites
        .replaceAll("\\s$", "") // Closing whites
    ;
    for (final String operator : new String[] { ":", "/", "%", ",", "\\{", "\\}", "=", ":", "\\?", ";", "\\+", ">", ">=", "!=", "==", "<", "<=", "-",
        "\\*", "\\|", "\\&", "%", "\\(", "\\)", "[\\^]" })
      $ = $ //
          .replaceAll(tide.WHITES + operator, operator) // Preceding whites
          .replaceAll(operator + tide.WHITES, operator) // Trailing whites
      ;
    return $;
  }
}
