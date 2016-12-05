package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** replaces for loops with ranges
 * @author Dan Abramovich
 * @since 2016 */
public final class ReplaceForWithRange extends Tipper<ForStatement> implements TipperCategory.Idiomatic {
  private static final List<UserDefinedTipper<ForStatement>> tippers = new ArrayList<>();

  public ReplaceForWithRange() {
    if (tippers.size() == 12)
      return;
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N < $L2; ++$N)$B", "for(Integer $N : range.from($L1).to($L2))$B",
        "replace non-inclusive for loop with the matching range"));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N <= $L2; ++$N)$B", "for(Integer $N : range.from($L1).to($L2).inclusive())$B",
        "replace inclusive for loop with the matching range"));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N < $L2; $N+=$L3)$B", "for(Integer $N : range.from($L1).step($L3).to($L2))$B",
        "replace non-inclusive for loop with the matching range"));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N <= $L2; $N+=$L3)$B",
        "for(Integer $N : range.from($L1).step($L3).to($L2).inclusive())$B", "replace inclusive for loop with the matching range"));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N > $L2; $N+=$L3)$B", "for(Integer $N : range.from($L1).step($L3).to($L2))$B",
        "replace non-inclusive for loop with the matching range"));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N >= $L2; $N+=$L3)$B",
        "for(Integer $N : range.from($L1).step($L3).to($L2).inclusive())$B", "replace inclusive for loop with the matching range"));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N < $L2; $N-=$L3)$B", "for(Integer $N : range.from($L1).step(-$L3).to($L2))$B",
        "replace non-inclusive for loop with the matching range"));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N <= $L2; $N-=$L3)$B",
        "for(Integer $N : range.from($L1).step(-$L3).to($L2).inclusive())$B", "replace inclusive for loop with the matching range"));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N > $L2; $N-=$L3)$B", "for(Integer $N : range.from($L1).step(-$L3).to($L2))$B",
        "replace non-inclusive for loop with the matching range"));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N >= $L2; $N-=$L3)$B",
        "for(Integer $N : range.from($L1).step(-$L3).to($L2).inclusive())$B", "replace inclusive for loop with the matching range"));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N > $L2; --$N)$B", "for(Integer $N:range.from($L1).step(-1).to($L2))$B",
        "replace non-inclusive for loop with the matching range"));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N >= $L2; --$N)$B",
        "for(Integer $N : range.from($L1).step(-1).to($L2).inclusive())$B", "replace inclusive for loop with the matching range"));
  }

  @Override public boolean canTip(final ForStatement s) {
    for (final UserDefinedTipper<ForStatement> ¢ : tippers)
      if (¢.canTip(s)) {
        final SimpleName n = az.simpleName(¢.getMatching(s, "$N"));
        if (n == null)
          continue;
        final Block b = az.block(¢.getMatching(s, "$B"));
        if (b == null)
          continue;
        if (!ChangedInBlock(n.getIdentifier(), b))
          return true;
      }
    return false;
  }

  private static boolean ChangedInBlock(final String id, final Block b) {
    final Bool a = new Bool();
    b.accept(new ASTVisitor() {
      @Override public boolean visit(final Assignment ¢) {
        if (iz.simpleName(left(¢)) && identifier(az.simpleName(left(¢))).equals(id))
          a.inner = true;
        return true;
      }

      // TODO: dan abramavitch this is not the way to check the kind of operator
      @Override public boolean visit(final PrefixExpression ¢) {
        if (("++".equals(¢.getOperator() + "") || "--".equals(¢.getOperator() + ""))
            && iz.simpleName(¢.getOperand()) && identifier(az.simpleName(¢.getOperand())).equals(id))
          a.inner = true;
        return true;
      }

      @Override public boolean visit(final PostfixExpression ¢) {
        if (("++".equals(¢.getOperator() + "") || "--".equals(¢.getOperator() + ""))
            && iz.simpleName(¢.getOperand()) && identifier(az.simpleName(¢.getOperand())).equals(id))
          a.inner = true;
        return true;
      }
    });
    return a.inner;
  }

  @Override public Tip tip(final ForStatement x) {
    for (final UserDefinedTipper<ForStatement> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.tip(x);
    return null;
  }

  @Override public String description(final ForStatement x) {
    for (final UserDefinedTipper<ForStatement> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.description(x);
    return null;
  }
}
