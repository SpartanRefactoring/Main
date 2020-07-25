package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.tipping.RemovingTipper;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** TODO Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class ModifierFinalAbstractMethodRedundant extends RemovingTipper<Modifier>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = 0x3057B6D28D0CEC1L;

  @Override public String description() {
    return "Remove redundant final modifier of paramaeter to abstract method";
  }
  @Override public String description(final Modifier ¢) {
    return "Remove redundant final '" + az.singleVariableDeclaration(parent(¢)) + "' (parameter to abstract method)";
  }
  @Override public boolean prerequisite(final Modifier m) {
    if (!m.isFinal())
      return false;
    final SingleVariableDeclaration v = az.singleVariableDeclaration(parent(m));
    if (v == null)
      return false;
    final MethodDeclaration $ = az.methodDeclaration(parent(v));
    return $ != null && body($) == null;
  }
}
