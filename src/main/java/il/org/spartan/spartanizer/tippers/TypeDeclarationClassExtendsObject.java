package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Converts <code>class C extends Object {...}</code> to
 * <code>class C {...}</code> to
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-15 */
public final class TypeDeclarationClassExtendsObject extends ReplaceCurrentNode<TypeDeclaration> implements TipperCategory.Abbreviation {
  @Override public ASTNode replacement(final TypeDeclaration d) {
    if (d.isInterface())
      return null;
    final Type t = d.getSuperclassType();
    if (t == null)
      return null;
    switch (t + "") {
      default:
        return null;
      case "Object":
      case "java.lang.Object":
        final TypeDeclaration $ = copy.of(d);
        $.setSuperclassType(null);
        return $;
    }
  }

  @Override public String description(final TypeDeclaration ¢) {
    return "Remove implicit extends " + ¢.getSuperclassType();
  }
}
