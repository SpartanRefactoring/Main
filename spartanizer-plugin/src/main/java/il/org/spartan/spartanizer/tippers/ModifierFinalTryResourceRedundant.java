package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.parent;

import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;

import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.tipping.RemovingTipper;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code abstract</b> <b>interface</b>a{}</code> to
 * {@code interface</b> a{}</code>, etc.
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class ModifierFinalTryResourceRedundant extends RemovingTipper<Modifier>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = -0x7E3F907D0969FD04L;

  @Override public String description() {
    return "Remove redundant final modifier of try resource";
  }
  @Override public String description(final Modifier ¢) {
    return description() + ": '" + az.variableDeclarationExpression(parent(¢)) + "'";
  }
  @Override public boolean prerequisite(final Modifier $) {
    if (!$.isFinal())
      return false;
    final VariableDeclarationExpression x = az.variableDeclarationExpression(parent($));
    return x != null && az.tryStatement(parent(x)) != null;
  }
}
