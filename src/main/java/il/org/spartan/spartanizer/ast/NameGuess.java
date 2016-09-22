package il.org.spartan.spartanizer.ast;

import org.eclipse.jdt.core.dom.*;

/** Quick hack to guess the kind of stuff a name denotes based on cameCasing and
 * other conventions
 * @author Yossi Gil
 * @year 2016 */
public enum NameGuess {
  CLASS_CONSTANT, //
  CLASS_NAME, //
  SETTTER_METHOD, //
  GETTER_METHOD, //
  IS_METHOD, //
  ANONYMOUS, //
  METHOD_OR_VARIABLE, //
  DOLLAR, //
  CENT, //
  WEIRDO, //
  UNKNOWN, //
  ;
  static NameGuess of(String nameOfSomething) {
    if (nameOfSomething == null || nameOfSomething.length() == 0)
      return null;
    if (nameOfSomething.matches("[_]+")) //
      return NameGuess.ANONYMOUS;
    if (nameOfSomething.matches("$")) //
      return NameGuess.DOLLAR;
    if (nameOfSomething.matches("¢")) //
      return NameGuess.CENT;
    if (nameOfSomething.matches("[_$¢]+")) //
      return NameGuess.WEIRDO;
    if (nameOfSomething.matches("[A-Z_$¢]+")) //
      return NameGuess.WEIRDO;
    if (nameOfSomething.matches("is[A-Z][a-zA-Z]*")) //
      return NameGuess.CLASS_CONSTANT;
    if (nameOfSomething.matches("set[A-Z][a-zA-Z]*")) //
      return NameGuess.SETTTER_METHOD;
    if (nameOfSomething.matches("get[A-Z][a-zA-Z]*")) //
      return NameGuess.GETTER_METHOD;
    if (nameOfSomething.matches("[A-Z][a-zA-Z]*")) //
      return NameGuess.CLASS_NAME;
    if (nameOfSomething.matches("[a-z][a-zA-Z]*")) //
      return NameGuess.CLASS_NAME;
    assert wizard.unreachable() : wizard.dump() + //
        "\n nameOfSomething=" + nameOfSomething + //
        wizard.endDump();
    return NameGuess.UNKNOWN;
  }

  public static boolean isClassName(String e) {
    return of(e) == CLASS_NAME;
  }

  public static boolean isClassName(ASTNode e) {
    return isClassName(hop.lastComponent(az.name(e)) + "");
  }
}