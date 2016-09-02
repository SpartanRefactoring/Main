package il.org.spartan.refactoring.engine;

import java.util.regex.*;

/** A utility parser that resolves a variable's short name, and determines
 * whether a pre-existing name is a generic variation of the type's name. <br>
 * A variable's short name is a single-character name, determined by the first
 * character in the last word of the type's name.<br>
 * For example: <code>
 *
 * <pre> public void execute(HTTPSecureConnection httpSecureConnection) {...}
 * </pre>
 *
 * </code> would become<br>
 * <code>
 *
 * <pre> public void execute(HTTPSecureConnection c) {...} </pre>
 *
 * </code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015-08-25 */
@SuppressWarnings("static-method") public class JavaTypeNameParser {
  /** The type name managed by this instance */
  public final String typeName;

  /** Instantiates this class
   * @param typeName the Java type name to parse
   * @param isCollection denotes whether the type is a collection or a variadic
   *        parameter */
  public JavaTypeNameParser(final String typeName) {
    this.typeName = typeName;
  }

  /** @return an abbreviation of the type name */
  public String abbreviate() {
    String a = "";
    final Matcher m = Pattern.compile("[A-Z]").matcher(typeName);
    while (m.find())
      a += m.group();
    return a.toLowerCase();
  }

  /** Returns whether a variable name is a generic variation of its type name. A
   * variable name is considered to be a generic variation of its type name if
   * the variable name is equal to the type name, either one of them is
   * contained within the other, or it is an abbreviation of the type name (For
   * example: <code>sb</code> is a generic variation of {@link StringBuilder})
   * @param variableName the name of the variable
   * @return true if the variable name is a generic variation of the type name,
   *         false otherwise */
  public boolean isGenericVariation(final String variableName) {
    return typeName.equalsIgnoreCase(variableName) || lowerCaseContains(typeName, variableName)
        || lowerCaseContains(typeName, toSingular(variableName)) || variableName.equals(abbreviate());
  }

  private boolean isLower(final int i) {
    return Character.isLowerCase(typeName.charAt(i));
  }

  /** Shorthand for n.equals(this.shortName())
   * @param s JD
   * @return true if the provided name equals the type's short name */
  public boolean isShort(final String s) {
    return s.equals(shortName());
  }

  private boolean isUpper(final int i) {
    return Character.isUpperCase(typeName.charAt(i));
  }

  String lastName() {
    return typeName.substring(lastNameIndex());
  }

  int lastNameIndex() {
    if (isUpper(typeName.length() - 1))
      return typeName.length() - 1;
    for (int $ = typeName.length() - 1; $ > 0; --$) {
      if (isLower($) && isUpper($ - 1))
        return $ - 1;
      if (isUpper($) && isLower($ - 1))
        return $;
    }
    return 0;
  }

  private boolean lowerCaseContains(final String s, final String substring) {
    return s.toLowerCase().contains(substring.toLowerCase());
  }

  /** Returns the calculated short name for the type
   * @return type's short name */
  public String shortName() {
    return lastNameCharIndex(0).equals("e") && lastNameCharIndex(1).equals("x") 
        ? "x" : lastNameCharIndex(0);
  }

  private String lastNameCharIndex(int i) {
    return lastName().length() < i + 1 ? "" : String.valueOf(Character.toLowerCase(lastName().charAt(i)));
  }

  private String toSingular(final String s) {
    return s == null ? null
        : s.endsWith("ies") ? s.substring(0, s.length() - 3) + "y"
            : s.endsWith("es") ? s.substring(0, s.length() - 2) : s.endsWith("s") ? s.substring(0, s.length() - 1) : s;
  }
}