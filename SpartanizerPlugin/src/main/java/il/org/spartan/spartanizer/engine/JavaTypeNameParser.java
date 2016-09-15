package il.org.spartan.spartanizer.engine;

import java.util.regex.*;

import org.eclipse.jdt.core.dom.*;

/** A utility parser that resolves a variable's short name, and determines
 * whether a pre-existing name is a generic variation of the type's name.
 * <p>
 * A variable's short name is a single-character name, determined by the first
 * character in the last word of the type's name.<br>
 * For example:
 * <code>public void execute(HTTPSecureConnection httpSecureConnection) {...}</code>
 * would become <code>public void execute(HTTPSecureConnection c) {...} </code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015-08-25 */
@SuppressWarnings("static-method") public class JavaTypeNameParser {
  public static boolean isJohnDoe(final SingleVariableDeclaration ¢) {
    return isJohnDoe(¢.getType(), ¢.getName());
  }

  public static boolean isJohnDoe(final String typeName, final String variableName) {
    return new JavaTypeNameParser(typeName).isGenericVariation(variableName);
  }

  static boolean isJohnDoe(final Type t, final SimpleName n) {
    return isJohnDoe(t + "", n.getIdentifier());
  }

  /** The type name managed by this instance */
  public final String typeName;

  public JavaTypeNameParser(final SimpleName ¢) {
    this(¢.getIdentifier());
  }

  public JavaTypeNameParser(final SingleVariableDeclaration ¢) {
    this(¢.getName());
  }

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

  public boolean isGenericVariation(final SingleVariableDeclaration ¢) {
    return isGenericVariation(¢.getName());
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

  /** Shorthand for n.equals(this.shortName())
   * @param s JD
   * @return true if the provided name equals the type's short name */
  public boolean isShort(final String ¢) {
    return ¢.equals(shortName());
  }

  // TODO: Dan, is this a hack? You were supposed to look at the way
  // "Expression" gets abbreviated to 'e'. Not turn every 'e' into an 'x'.
  //
  //
  // Yossi, I must have misunderstood your intention. In my understanding, in
  // issue 31 you
  // asked of me to rename identifiers whose typename's last word begins with
  // 'ex' to 'x' instead of 'e'.
  //
  // This cannot be done in a seperate wring, to the best of my understanding:
  // We cannot have two wrings that change
  // the identifier in different ways, and each keeping the other wring
  // applicable.
  //
  // The way to do that, in my opinion, is to change the renaming rules on a
  // fundamental level - that is, here, or in spartan.java.
  // I cannot see how can this be done in any way other than hard-coding it.
  /** Returns the calculated short name for the type
   * @return type's short name */
  public String shortName() {
    return "e".equals(lastNameCharIndex(0)) && "x".equals(lastNameCharIndex(1)) ? "x" : lastNameCharIndex(0);
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
    return lastName().length() < ¢ + 1 ? "" : String.valueOf(Character.toLowerCase(lastName().charAt(¢)));
  }

  private boolean lowerCaseContains(final String s, final String substring) {
    return s.toLowerCase().contains(substring.toLowerCase());
  }

  // TODO: Yossi, looks better :). I want to die with this Mitug stuff. Did you
  // know that clock
  // is two transistors and a capacitor? Cycle duty depends on the
  // capacitance:@.
  private String toSingular(final String word) {
    final String $ = word.replaceAll("ies$", "y").replaceAll("es$", "").replaceAll("s$", "");
    return $;
  }
}