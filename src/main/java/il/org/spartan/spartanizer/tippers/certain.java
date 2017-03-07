package il.org.spartan.spartanizer.tippers;

import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** Fluent API
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2017-02-08 */
public interface certain {
  static boolean string(final ASTNode... ¢) {
    return Stream.of(¢).allMatch(certain::string);
  }

  static boolean string(final ASTNode ¢) {
    return string(az.expression(¢));
  }

  static boolean string(final Expression ¢) {
    return ¢ != null && (iz.stringLiteral(¢) || type.isString(¢) || iz.name(¢) && wizard.isString(analyze.type(az.simpleName(¢))));
  }
}
