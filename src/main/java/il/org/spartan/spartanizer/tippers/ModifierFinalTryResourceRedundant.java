package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert <code><b>abstract</b> <b>interface</b>a{}</code> to
 * <code><b>interface</b> a{}</code>, etc.
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2015-07-29 */
public final class ModifierFinalTryResourceRedundant extends RemovingTipper<Modifier>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = -9097148639014944004L;

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
