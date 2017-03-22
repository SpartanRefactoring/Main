package il.org.spartan.spartanizer.engine.nominal;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Fluent API.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-05 */
public interface JohnDoe {
  static boolean property(@Nullable final SingleVariableDeclaration ¢) {
    return ¢ != null && property(type(¢), name(¢));
  }

  static boolean property(final String typeName, @NotNull final String variableName) {
    return JavaTypeNameParser.make(typeName).isGenericVariation(variableName);
  }

  static boolean property(final Type t, @Nullable final SimpleName n) {
    return n != null && property(t + "", identifier(n));
  }
}
