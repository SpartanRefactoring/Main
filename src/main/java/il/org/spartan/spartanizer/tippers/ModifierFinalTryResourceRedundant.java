package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** convert <code><b>abstract</b> <b>interface</b>a{}</code> to
 * <code><b>interface</b> a{}</code>, etc.
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class ModifierFinalTryResourceRedundant extends RemovingTipper<Modifier>//
    implements TipperCategory.SyntacticBaggage {
  @Override public String description() {
    return "Remove redundant final modifier of try resource";
  }

  @Override @NotNull public String description(final Modifier ¢) {
    return description() + ": '" + az.variableDeclarationExpression(parent(¢)) + "'";
  }

  @Override public boolean prerequisite(@NotNull final Modifier $) {
    if (!$.isFinal())
      return false;
    final VariableDeclarationExpression x = az.variableDeclarationExpression(parent($));
    return x != null && az.tryStatement(parent(x)) != null;
  }
}
