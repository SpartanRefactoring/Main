package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.leonidas.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Ori Marcovitch
 * @since 2016 */
public final class InfixIndexOfToStringContains extends Tipper<InfixExpression> implements TipperCategory.Idiomatic {
  static List<UserDefinedTipper<InfixExpression>> tippers = new ArrayList<>();

  public InfixIndexOfToStringContains() {
    if (tippers.size() == 4)
      return;
    tippers.add(TipperFactory.tipper("$X1.indexOf($X2) >= 0", "$X1.contains($X2)", "replace indexOf >= 0 with contains"));
    tippers.add(TipperFactory.tipper("$X1.indexOf($X2) < 0", "!$X1.contains($X2)", "replace indexOf < 0 with !contains"));
    tippers.add(TipperFactory.tipper("$X1.indexOf($X2) != -1", "$X1.contains($X2)", "replace indexOf != -1 with contains"));
    tippers.add(TipperFactory.tipper("$X1.indexOf($X2) == -1", "!$X1.contains($X2)", "replace indexOf == -1 with !contains"));
  }

  /** @see il.org.spartan.spartanizer.tipping.Tipper#canTip(org.eclipse.jdt.core.dom.ASTNode) */
  @Override public boolean canTip(final InfixExpression x) {
    if (!stringOperands(x))
      return false;
    for (final UserDefinedTipper<InfixExpression> ¢ : tippers)
      if (¢.canTip(x))
        return true;
    return false;
  }

  /** @param x
   * @return */
  private static boolean stringOperands(final InfixExpression x) {
    if (!iz.methodInvocation(x.getLeftOperand())
        || az.methodInvocation(x.getLeftOperand()).getExpression() != null && !type.isString(az.methodInvocation(x.getLeftOperand()).getExpression()))
      return false;
    @SuppressWarnings("unchecked") final List<ASTNode> arguments = az.methodInvocation(x.getLeftOperand()).arguments();
    return !arguments.isEmpty() && iz.expression(arguments.get(0)) && type.isString(az.expression(arguments.get(0)));
  }

  @Override public Tip tip(final InfixExpression x) throws TipperFailure {
    for (final UserDefinedTipper<InfixExpression> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.tip(x);
    return null;
  }

  /** @see il.org.spartan.spartanizer.tipping.Tipper#description(org.eclipse.jdt.core.dom.ASTNode) */
  @Override public String description(final InfixExpression x) {
    for (final UserDefinedTipper<InfixExpression> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.description(x);
    return null;
  }
}
