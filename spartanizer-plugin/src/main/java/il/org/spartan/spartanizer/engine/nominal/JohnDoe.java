package il.org.spartan.spartanizer.engine.nominal;

import static il.org.spartan.spartanizer.ast.navigate.step.identifier;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.type;

import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

import fluent.ly.is;

/** Fluent API.
 * @author Yossi Gil
 * @since 2017-01-05 */
public interface JohnDoe {
  static boolean property(final SingleVariableDeclaration ¢) {
    return ¢ != null && property(type(¢), name(¢));
  }
  static boolean property(final String typeName, final String variableName) {
    return JavaTypeNameParser.make(typeName).isGenericVariation(variableName);
  }
  static boolean property(final Type t, final SimpleName n) {
    return n != null && property(t + "", identifier(n));
  }
  static boolean property(final String ¢) {
    return ¢.length() == 1 || is.in(¢, shortNames);
  }

  String[] shortNames = { "idx", "arr", "iter", "lst", "integer", "list" };
}
