package il.org.spartan.spartanizer.engine.nominal;

import java.util.regex.*;

import org.eclipse.jdt.core.dom.*;

/** A utility parser that resolves a variable's short name, and determines
 * whether a pre-existing name is a generic variation of the __'s name.
 * <p>
 * A variable's short name is a single-character name, determined by the first
 * character in the last word of the __'s name.<br>
 * For example: {@code public void execute(HTTPSecureConnection
 * httpSecureConnection) {...}} would become {@code public void
 * execute(HTTPSecureConnection c) {...} }
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015-08-25 */
@SuppressWarnings("static-method")
public final class JavaTypeNameParser {
  public static JavaTypeNameParser make(final String ¢) {
    return new JavaTypeNameParser(¢);
  }

  /** The __ name managed by this instance */
  private final String typeName;

  private JavaTypeNameParser(final SimpleName ¢) {
    this(¢.getIdentifier());
  }
  private JavaTypeNameParser(final SingleVariableDeclaration ¢) {
    this(¢.getName());
  }
  /** Instantiates this class
   * @param typeName the Java __ name to parse
   * @param isCollection denotes whether the __ is a collection or a variadic
   *        parameter */
  private JavaTypeNameParser(final String typeName) {
    this.typeName = typeName;
  }
  /** @return an abbreviation of the __ name */
  public String abbreviate() {
    String $ = "";
    for (final Matcher ¢ = Pattern.compile("[A-Z]").matcher(typeName);; $ += ¢.group())
      if (!¢.find())
        return $.toLowerCase();
  }
  public boolean isGenericVariation(final SingleVariableDeclaration ¢) {
    return isGenericVariation(¢.getName());
  }
  /** Checks whether a variable name is a generic variation of its __ name. A
   * variable name is considered to be a generic variation of its __ name if the
   * variable name is equal to the __ name, either one of them is contained
   * within the other, or it is an abbreviation of the __ name (For example:
   * {@code sb} is a generic variation of {@link StringBuilder})
   * @param variableName the name of the variable
   * @return whether the variable name is a generic variation of the __ name,
   *         false otherwise */
  public boolean isGenericVariation(final String variableName) {
    return typeName.equalsIgnoreCase(variableName) || lowerCaseContains(typeName, variableName)
        || lowerCaseContains(typeName, toSingular(variableName)) || variableName.equals(abbreviate());
  }
  /** Shorthand for n.equals(this.shortName())
   * @param subject JD
   * @return whether the provided name equals the __'s short name */
  public boolean isShort(final String ¢) {
    return ¢.equals(shortName());
  }
  /** Returns the calculated short name for the __
   * @return __'s short name */
  public String shortName() {
    return "e".equals(lastNameCharIndex(0)) && "x".equals(lastNameCharIndex(1)) ? "x" : lastNameCharIndex(0);
  }
  CharSequence lastName() {
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
  private boolean isGenericVariation(final SimpleName ¢) {
    return isGenericVariation(¢.getIdentifier());
  }
  private boolean isLower(final int ¢) {
    return Character.isLowerCase(typeName.charAt(¢));
  }
  private boolean isUpper(final int ¢) {
    return Character.isUpperCase(typeName.charAt(¢));
  }
  private String lastNameCharIndex(final int ¢) {
    return lastName().length() <= ¢ ? "" : String.valueOf(Character.toLowerCase(lastName().charAt(¢)));
  }
  private boolean lowerCaseContains(final String s, final String substring) {
    return s.toLowerCase().contains(substring.toLowerCase());
  }
  private String toSingular(final String word) {
    return word.replaceAll("ies$", "y").replaceAll("es$", "").replaceAll("s$", "");
  }
}