package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.parent;
import static il.org.spartan.spartanizer.research.TipperFactory.statementsPattern;

import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ForStatement;

import fluent.ly.as;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.research.UserDefinedTipper;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

/** Like {@link FindFirst} but for ForStatement
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-25 */
public final class ForLoop {
  public static class FindFirst extends NanoPatternTipper<ForStatement> {
    private static final long serialVersionUID = 0x6A9656558A3C696L;
    private static final String description = "Go Fluent : FindFirst";
    private static final List<UserDefinedTipper<Block>> tippers = as.list(
        statementsPattern("for ($T $N = $X1; $X2; $X3) if ($X4) return $N; return $L;", //
            "return from($X1).step(($N)->$X2).to(($N)->$X3).findFirst($N -> $X4).orElse($L);", description),
        statementsPattern("for ($T $N = $X1; $X2; $X3) if ($X4) return $N; throw $X6;", //
            "return from($X1).step(($N)->$X2).to(($N)->$X3).findFirst($N -> $X4).orElseThrow(()->$X6);", description),
        statementsPattern("for ($T $N1 = $X1; $X2; $X3) if ($X4) {$N2 = $N; break;}", //
            "$N2 = from($X1).step(($N)->$X2).to(($N)->$X3).findFirst($N -> $X4).orElse($N2);", description),
        statementsPattern("for ($T $N = $X1; $X2; $X3) if ($X4) return $X5; return $L;", //
            "return from($X1).step(($N)->$X2).to(($N)->$X3).findFirst($N -> $X4).map(($N)->$X5).orElse($L);", description),
        statementsPattern("for ($T $N = $X1; $X2; $X3) if ($X4) return $X5; throw $X6;", //
            "return from($X1).step(($N)->$X2).to(($N)->$X3).findFirst($N -> $X4).map(($N)->$X5).orElseThrow(()->$X6);", description),
        statementsPattern("for ($T $N1 = $X1; $X2; $X3) if ($X4) {$N2 = $X5; break;}", //
            "$N2 = from($X1).step(($N)->$X2).to(($N)->$X3).findFirst($N -> $X4).map(($N)->$X5).orElse($N2);", description),
        statementsPattern("for ($T $N = $X1; $X2; $X3) if ($X4) return $N", //
            "returnFirstIfAny(from($X1).step(($N)->$X2).to(($N)->$X3)).matches($N -> $X4);", description),
        statementsPattern("for ($T $N = $X1; $X2; $X3) if ($X4) return $X5;", //
            "returnFirstIfAny(from($X1).step(($N)->$X2).to(($N)->$X3)).matches($N -> $X4).map($X5);", description));

    @Override public boolean canTip(final ForStatement x) {
      return anyTips(tippers, az.block(parent(x)))//
      ;
    }
    @Override public Tip pattern(final ForStatement $) {
      return firstTip(tippers, az.block(parent($)));
    }
    @Override public String tipperName() {
      return "FirstSuchThat";
    }
    @Override public String description() {
      return "Iterate a collection for the first element matching some predicate";
    }
  }
}
