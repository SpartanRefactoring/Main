package il.org.spartan.spartanizer.engine.nominal;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Fluent API.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-05 */
public interface JohnDoe {
  static boolean property(final SingleVariableDeclaration ¢) {
    return ¢ != null && property(step.type(¢), ¢.getName());
  }

  static boolean property(final String typeName, final String variableName) {
    return JavaTypeNameParser.make(typeName).isGenericVariation(variableName);
  }

  static boolean property(final Type t, final SimpleName n) {
    return n != null && property(t + "", n.getIdentifier());
  }
}
