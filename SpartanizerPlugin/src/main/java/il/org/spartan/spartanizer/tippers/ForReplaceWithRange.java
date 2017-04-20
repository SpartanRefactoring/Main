package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** replaces for loops with ranges
 * @author Dan Abramovich
 * @since 2016 */
public final class ForReplaceWithRange extends Tipper<ForStatement>//
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = -0x7AFE12D1A8CD3E90L;
  private static final String DESCRIPTION_NON_INCLUSIVE = "replace inclusive for loop with the matching range";
  private static final String DESCRIPTION_INCLUSIVE = "replace non-inclusive for loop with the matching range";
  private static final Collection<UserDefinedTipper<ForStatement>> tippers = an.empty.list();

  public ForReplaceWithRange() {
    if (!tippers.isEmpty())
      return;
    tippers.add(
        TipperFactory.patternTipper("for(int $N = $L1; $N < $L2; ++$N)$B", "for(Integer $N : range.from($L1).to($L2))$B", DESCRIPTION_INCLUSIVE));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N <= $L2; ++$N)$B", "for(Integer $N : range.from($L1).to($L2).inclusive())$B",
        DESCRIPTION_NON_INCLUSIVE));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N < $L2; $N+=$L3)$B", "for(Integer $N : range.from($L1).step($L3).to($L2))$B",
        DESCRIPTION_INCLUSIVE));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N <= $L2; $N+=$L3)$B",
        "for(Integer $N : range.from($L1).step($L3).to($L2).inclusive())$B", DESCRIPTION_NON_INCLUSIVE));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N > $L2; $N+=$L3)$B", "for(Integer $N : range.from($L1).step($L3).to($L2))$B",
        DESCRIPTION_INCLUSIVE));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N >= $L2; $N+=$L3)$B",
        "for(Integer $N : range.from($L1).step($L3).to($L2).inclusive())$B", DESCRIPTION_NON_INCLUSIVE));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N < $L2; $N-=$L3)$B", "for(Integer $N : range.from($L1).step(-$L3).to($L2))$B",
        DESCRIPTION_INCLUSIVE));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N <= $L2; $N-=$L3)$B",
        "for(Integer $N : range.from($L1).step(-$L3).to($L2).inclusive())$B", DESCRIPTION_NON_INCLUSIVE));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N > $L2; $N-=$L3)$B", "for(Integer $N : range.from($L1).step(-$L3).to($L2))$B",
        DESCRIPTION_INCLUSIVE));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N >= $L2; $N-=$L3)$B",
        "for(Integer $N : range.from($L1).step(-$L3).to($L2).inclusive())$B", DESCRIPTION_NON_INCLUSIVE));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N > $L2; --$N)$B", "for(Integer $N:range.from($L1).step(-1).to($L2))$B",
        DESCRIPTION_INCLUSIVE));
    tippers.add(TipperFactory.patternTipper("for(int $N = $L1; $N >= $L2; --$N)$B",
        "for(Integer $N : range.from($L1).step(-1).to($L2).inclusive())$B", DESCRIPTION_NON_INCLUSIVE));
  }

  @Override public boolean canTip(final ForStatement s) {
    for (final UserDefinedTipper<ForStatement> ¢ : tippers)
      if (¢.check(s)) {
        final SimpleName n = az.simpleName(¢.getMatching(s, "$N"));
        if (n == null)
          continue;
        final Block b = az.block(¢.getMatching(s, "$B"));
        if (b == null)
          continue;
        if (!ChangedInBlock(identifier(n), b))
          return true;
      }
    return false;
  }

  private static boolean ChangedInBlock(final String id, final Block b) {
    final Bool $ = new Bool();
    // noinspection SameReturnValue,SameReturnValue,SameReturnValue
    b.accept(new ASTVisitor(true) {
      @Override public boolean visit(final Assignment ¢) {
        if (iz.simpleName(left(¢)) && identifier(az.simpleName(left(¢))).equals(id))
          $.inner = true;
        return true;
      }

      @Override public boolean visit(final PrefixExpression ¢) {
        if (iz.updater(¢) && iz.simpleName(operand(¢)) && identifier(az.simpleName(operand(¢))).equals(id))
          $.inner = true;
        return true;
      }

      @Override public boolean visit(final PostfixExpression ¢) {
        if (("++".equals(operator(¢) + "") || "--".equals(operator(¢) + "")) && iz.simpleName(operand(¢))
            && identifier(az.simpleName(operand(¢))).equals(id))
          $.inner = true;
        return true;
      }
    });
    return $.inner;
  }

  @Override public Tip tip(final ForStatement x) {
    return tippers.stream().filter(λ -> λ.check(x)).map(λ -> λ.tip(x)).findFirst().orElse(null);
  }

  @Override public String description(final ForStatement x) {
    return tippers.stream().filter(λ -> λ.check(x)).map(λ -> λ.description(x)).findFirst().orElse(null);
  }
}
