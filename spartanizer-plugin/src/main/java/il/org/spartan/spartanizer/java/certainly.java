package il.org.spartan.spartanizer.java;

import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** Fluent API, determines whether an expression must be a string.
 * @author Yossi Gil
 * @since 2017-02-08 */
public enum certainly {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  static boolean string(final ASTNode... ¢) {
    return Stream.of(¢).allMatch(certainly::string);
  }
  static boolean string(final ASTNode ¢) {
    return string(az.expression(¢));
  }
  static boolean string(final Expression ¢) {
    return ¢ != null && (iz.stringLiteral(¢) || type.isString(¢) || iz.name(¢) && type.isString(analyze.type(az.simpleName(¢))));
  }
}
