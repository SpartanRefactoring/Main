package il.org.spartan.spartanizer.engine.nominal;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;

/** Quick hack to guess the kind of stuff a name denotes based on cameCasing and
 * other conventions
 * @author Yossi Gil
 * @since Jan 5, 2017 */
public enum guessName {
  STATIC_CONSTANT, //
  CLASS_NAME() {
    @Override boolean special(final String name) {
      return is.in(name, "extract", "into", "azzert", "iz", "az", "is", "az", "has", "haz");
    }
  }, //
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
    return ¢ != null && isClassName(hop.lastName(az.name(¢)) + "");
  }
  public static boolean isClassName(final String e) {
    return of(e) == CLASS_NAME;
  }
  public static guessName of(final String nameOfSomething) {
    if (nameOfSomething == null || nameOfSomething.isEmpty())
      return null;
    for (final guessName $ : guessName.values())
      if ($.special(nameOfSomething))
        return $;
    if (nameOfSomething.matches("[_]+")) //
      return guessName.ANONYMOUS;
    if (nameOfSomething.matches("[$]*")) //
      return guessName.DOLLAR;
    if (nameOfSomething.matches("¢*")) //
      return guessName.CENT;
    if (nameOfSomething.matches("[_$¢]+")) //
      return guessName.WEIRDO;
    if (nameOfSomething.matches("[A-Z][_A-Z0-9]*")) //
      return guessName.STATIC_CONSTANT;
    if (nameOfSomething.matches("is[A-Z][A-Z0-9_]*")) //
      return guessName.IS_METHOD;
    if (nameOfSomething.matches("set[A-Z][a-zA-Z0-9]*")) //
      return guessName.SETTTER_METHOD;
    if (nameOfSomething.matches("get[A-Z][a-zA-Z0-9]*")) //
      return guessName.GETTER_METHOD;
    if (nameOfSomething.matches("[$A-Z][a-zA-Z0-9]*")) //
      return guessName.CLASS_NAME;
    if (nameOfSomething.matches("[a-z_][_a-zA-Z0-9]*")) //
      return guessName.METHOD_OR_VARIABLE;
    assert fault.unreachable() : fault.dump() + //
        "\n nameOfSomething=" + nameOfSomething + //
        fault.done();
    return guessName.UNKNOWN;
  }
  @SuppressWarnings("static-method") boolean special(@SuppressWarnings("unused") final String nameOfSomething) {
    return false;
  }
}
