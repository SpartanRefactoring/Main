package il.org.spartan.spartanizer.utils.tdd;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch */
/** @author AnnaBel7
 * @author michalcohen
 * @since Nov 4, 2016 */
/** @author Shay Segal
 * @author Sefi Albo
 * @author Daniel Shames */
public enum find {
  ;
  /** @author AnnaBel7
   * @author michalcohena
   * @since Nov 4, 2016 */
  public static TypeDeclaration ancestorType(final ASTNode n) {
    if (n == null)
      return null;
    ASTNode $ = n.getParent();
    for (; $ != null && az.typeDeclaration($) == null; $ = $.getParent()) {
      // In the end of the for loop, a will be null or the enclosing Type
      // declaration.
    }
    return (TypeDeclaration) $;
  }

  public static MethodDeclaration ancestorMethod(final ASTNode n) {
    ASTNode $ = n;
    if ($ == null)
      return null;
    while ($ != null) {
      if (az.methodDeclaration($) != null && az.lambdaExpression(az.methodDeclaration($)) == null)
        return az.methodDeclaration($);
      $ = $.getParent();
    }
    return null;
  }
}
