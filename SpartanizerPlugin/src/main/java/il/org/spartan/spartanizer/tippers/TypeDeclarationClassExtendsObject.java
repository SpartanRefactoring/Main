package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** Converts {@code class C extends Object {...}} to {@code class C {...}} to
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-15 */
public final class TypeDeclarationClassExtendsObject extends ReplaceCurrentNode<TypeDeclaration>//
    implements TipperCategory.SyntacticBaggage {
  @Override public ASTNode replacement(@NotNull final TypeDeclaration ¢) {
    if (¢.isInterface() || !wizard.isObject(¢.getSuperclassType()))
      return null;
    final TypeDeclaration $ = copy.of(¢);
    $.setSuperclassType(null);
    return $;
  }

  @Override @NotNull public String description(@NotNull final TypeDeclaration ¢) {
    return "Trim implicit extends " + ¢.getSuperclassType();
  }

  @Override public String description() {
    return "Trim implicit extends Object";
  }
}
