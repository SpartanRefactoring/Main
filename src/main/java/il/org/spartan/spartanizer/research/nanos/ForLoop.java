package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Like {@link FindFirst} but for ForStatement
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-25 */
public final class ForLoop {
  public static class FindFirst extends NanoPatternTipper<ForStatement> {
    private static final long serialVersionUID = 480026321244898966L;
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

    @Nullable @Override public Tip pattern(final ForStatement $) {
      return firstTip(tippers, az.block(parent($)));
    }

    @NotNull @Override public String nanoName() {
      return "FirstSuchThat";
    }

    @NotNull @Override public String description() {
      return "Iterate a collection for the first element matching some predicate";
    }
  }
}
