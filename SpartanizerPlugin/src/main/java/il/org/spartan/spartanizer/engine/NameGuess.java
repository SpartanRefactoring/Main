package il.org.spartan.spartanizer.engine;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

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
  public static boolean isClassName(final ASTNode ¢) {
    return ¢ != null && isClassName(hop.lastComponent(az.name(¢)) + "");
  }

  public static boolean isClassName(final String e) {
    return of(e) == CLASS_NAME;
  }

  public static NameGuess of(final String nameOfSomething) {
    if (nameOfSomething == null || nameOfSomething.length() == 0)
      return null;
    if (nameOfSomething.matches("[_]+")) //
      return NameGuess.ANONYMOUS;
    if (nameOfSomething.matches("[$]*")) //
      return NameGuess.DOLLAR;
    if (nameOfSomething.matches("¢*")) //
      return NameGuess.CENT;
    if (nameOfSomething.matches("[_$¢]+")) //
      return NameGuess.WEIRDO;
    if (nameOfSomething.matches("[A-Z][_A-Z0-9]*")) //
      return NameGuess.CLASS_CONSTANT;
    if (nameOfSomething.matches("is[A-Z][A-Z0-9_]*")) //
      return NameGuess.IS_METHOD;
    if (nameOfSomething.matches("set[A-Z][a-zA-Z0-9]*")) //
      return NameGuess.SETTTER_METHOD;
    if (nameOfSomething.matches("get[A-Z][a-zA-Z0-9]*")) //
      return NameGuess.GETTER_METHOD;
    if (nameOfSomething.matches("[$A-Z][a-zA-Z0-9]*")) //
      return NameGuess.CLASS_NAME;
    if (nameOfSomething.matches("[a-z_][_a-zA-Z0-9]*")) //
      return NameGuess.METHOD_OR_VARIABLE;
    assert fault.unreachable() : fault.dump() + //
        "\n nameOfSomething=" + nameOfSomething + //
        fault.done();
    return NameGuess.UNKNOWN;
  }
}