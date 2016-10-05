package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.leonidas.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Ori Marcovitch
 * @since 2016 */
public final class InfixIndexOfToStringContains extends Tipper<InfixExpression> implements TipperCategory.Idiomatic {
  List<UserDefinedTipper<InfixExpression>> tippers = new ArrayList<>();

  public InfixIndexOfToStringContains() {
    tippers.add(TipperFactory.tipper("$X1.indexOf($X2) >= 0", "$X1.contains($X2)", "replace indexOf >= 0 with contains"));
    tippers.add(TipperFactory.tipper("$X1.indexOf($X2) < 0", "!$X1.contains($X2)", "replace indexOf < 0 with !contains"));
    tippers.add(TipperFactory.tipper("$X1.indexOf($X2) != -1", "$X1.contains($X2)", "replace indexOf != -1 with contains"));
    tippers.add(TipperFactory.tipper("$X1.indexOf($X2) == -1", "!$X1.contains($X2)", "replace indexOf == -1 with !contains"));
  }

  /** @see il.org.spartan.spartanizer.tipping.Tipper#canTip(org.eclipse.jdt.core.dom.ASTNode) */
  @Override public boolean canTip(InfixExpression x) {
    for (UserDefinedTipper<InfixExpression> ¢ : tippers)
      if (¢.canTip(x))
        return true;
    return false;
  }

  @Override public Tip tip(InfixExpression x) throws TipperFailure {
    for (UserDefinedTipper<InfixExpression> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.tip(x);
    return null;
  }

  /** @see il.org.spartan.spartanizer.tipping.Tipper#description(org.eclipse.jdt.core.dom.ASTNode) */
  @Override public String description(InfixExpression x) {
    for (UserDefinedTipper<InfixExpression> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.description(x);
    return null;
  }
}
