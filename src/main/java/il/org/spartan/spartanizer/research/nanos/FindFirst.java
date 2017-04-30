package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** There are actually few forms of FindFirst<br>
 * If none, can return null,some default value or throw. <br>
 * Sometimes returned value is mapped, or a filed of it is extracted. <br>
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class FindFirst extends NanoPatternTipper<EnhancedForStatement> {
  private static final long serialVersionUID = -0x643F3CD7DB6A7BA3L;
  private static final String description = "Go Fluent : FindFirst";
  private static final List<UserDefinedTipper<Block>> tippers = as.list(
      statementsPattern("for($T $N : $X1) if($X2) return $N; return $L;", //
          "return $X1.stream().filter($N -> $X2).findFirst().orElse($L);", description),
      statementsPattern("for($T $N : $X1) if($X2) return $X3; return $L;",
          "return $X1.stream().filter($N -> $X2).map($N -> $X3).findFirst().orElse($L);", description),
      statementsPattern("for($T $N : $X1) if($X2) return $N;  throw $X4;", //
          "return $X1.stream().filter($N -> $X2).findFirst().orElseThrow(()->$X4);", description),
      statementsPattern("for($T $N : $X1) if($X2) return $X3;  throw $X4;",
          "return $X1.stream().filter($N -> $X2).map($N -> $X3).findFirst().orElseThrow(()->$X4);", description),
      statementsPattern("for($T $N1 : $X1) if($X2) {$N2 = $N1; break;}", //
          "$N2 = $X1.stream().filter($N1 -> $X2).findFirst().orElse($N2);", description),
      statementsPattern("for($T $N1 : $X1) if($X2) {$N2 = $X3; break;}",
          "$N2 = $X1.stream().filter($N1 -> $X2).map($N1 -> $X3).findFirst().orElse($N2);", description),
      statementsPattern("for($T $N : $X1) if($X2) return $N;", //
          "returnFirstIfAny($X1).matches($N -> $X2);", description),
      statementsPattern("for($T $N : $X1) if($X2) return $X3;", //
          "returnFirstIfAny($X1).matches($N -> $X2).map($N -> $X3);", description));
  private static final Collection<NanoPatternTipper<EnhancedForStatement>> rivals = as.list(new HoldsForAll(), new HoldsForAny());

  @Override public boolean canTip(final EnhancedForStatement x) {
    return anyTips(tippers, az.block(parent(x)))//
        && nonTips(rivals, x)//
    ;
  }

  @Override public Tip pattern(final EnhancedForStatement $) {
    return firstTip(tippers, az.block(parent($)));
  }

  @Override public String description() {
    return "Iterate a collection for the first element matching some predicate";
  }

  @Override public String tipperName() {
    return "FirstSuchThat";
  }
}
