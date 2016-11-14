package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Ori Marcovitch
 * @since 2016 */
public final class InfixIndexOfToStringContains extends Tipper<InfixExpression> implements TipperCategory.Idiomatic {
  private static final List<UserDefinedTipper<InfixExpression>> tippers = new ArrayList<>();

  public InfixIndexOfToStringContains() {
    if (tippers.size() == 4)
      return;
    tippers.add(TipperFactory.tipper("$X1.indexOf($X2) >= 0", "$X1.contains($X2)", "replace indexOf>= 0 with contains"));
    tippers.add(TipperFactory.tipper("$X1.indexOf($X2) < 0", "!$X1.contains($X2)", "replace indexOf <0 with !contains"));
    tippers.add(TipperFactory.tipper("$X1.indexOf($X2) != -1", "$X1.contains($X2)", "replace indexOf != -1 with contains"));
    tippers.add(TipperFactory.tipper("$X1.indexOf($X2) == -1", "!$X1.contains($X2)", "replace indexOf == -1 with !contains"));
    tippers.add(TipperFactory.tipper("$X1.indexOf($X2) <= -1", "!$X1.contains($X2)", "replace indexOf == -1 with !contains"));
  }

  @Override public boolean canTip(final InfixExpression x) {
    for (final UserDefinedTipper<InfixExpression> ¢ : tippers)
      if (¢.canTip(x) && stringOperands(¢.getMatching(x, "$X1"), ¢.getMatching(x, "$X2")))
        return true;
    return false;
  }

  private static boolean stringOperands(final ASTNode n1, final ASTNode n2) {
    return stringOperand(n1) && stringOperand(n2);
  }

  private static boolean stringOperand(final ASTNode ¢) {
    return iz.name(¢) && isStringType(¢) || iz.stringLiteral(¢);
  }

  private static boolean isStringType(final ASTNode ¢) {
    return "String".equals(analyze.type(az.simpleName(¢)));
  }

  @Override public Tip tip(final InfixExpression x) {
    for (final UserDefinedTipper<InfixExpression> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.tip(x);
    return null;
  }

  @Override public String description(final InfixExpression x) {
    for (final UserDefinedTipper<InfixExpression> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.description(x);
    return null;
  }
}
