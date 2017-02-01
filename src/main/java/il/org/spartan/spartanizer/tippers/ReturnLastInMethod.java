package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** removes empty return, provided that it is last in method.
 * @author Yossi Gil
 * @since 2015-7-17 */
public final class ReturnLastInMethod extends RemovingTipper<ReturnStatement>//
    implements TipperCategory.SyntacticBaggage {
  @Override public String description(@SuppressWarnings("unused") final ReturnStatement __) {
    return "Remove redundant return statement";
  }

  @Override public boolean prerequisite(@NotNull final ReturnStatement ¢) {
    if (¢.getExpression() != null)
      return false;
    final Block $ = az.block(¢.getParent());
    return $ != null && lastIn(¢, statements($)) && iz.methodDeclaration(parent($));
  }
}
