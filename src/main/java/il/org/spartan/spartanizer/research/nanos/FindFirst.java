package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** There are actually few forms of FindFirst<br>
 * If none, can return null,some default value or throw. <br>
 * Sometimes returned value is mapped, or a filed of it is extracted. <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class FindFirst extends NanoPatternTipper<EnhancedForStatement> {
  private static final String description = "Go Fluent : FindFirst";
  private static final List<UserDefinedTipper<Block>> tippers = new ArrayList<UserDefinedTipper<Block>>() {
    static final long serialVersionUID = 1L;
    {
      add(statementsPattern("for($T $N : $X1) if($X2) return $N; return $L;", //
          "return $X1.stream().filter($N -> $X2).findFirst().orElse($L);", description));
      add(statementsPattern("for($T $N : $X1) if($X2) return $X3; return $L;",
          "return $X1.stream().filter($N -> $X2).map($N -> $X3).findFirst().orElse($L);", description));
      add(statementsPattern("for($T $N : $X1) if($X2) return $N;  throw $X4;", //
          "return $X1.stream().filter($N -> $X2).findFirst().orElseThrow(()->$X4);", description));
      add(statementsPattern("for($T $N : $X1) if($X2) return $X3;  throw $X4;",
          "return $X1.stream().filter($N -> $X2).map($N -> $X3).findFirst().orElseThrow(()->$X4);", description));
      add(statementsPattern("for($T $N : $X1) if($X2) {$N2 = $N; break;}", //
          "$N2 = $X1.stream().filter($N -> $X2).findFirst().orElse($N2);", description));
      add(statementsPattern("for($T $N : $X1) if($X2) {$N2 = $X3; break;}",
          "$N2 = $X1.stream().filter($N -> $X2).map($N -> $X3).findFirst().orElse($N2);", description));
    }
  };
  private static final List<NanoPatternTipper<EnhancedForStatement>> rivals = new ArrayList<NanoPatternTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(new ReturnHoldsForAll());
      add(new ReturnHoldsForAny());
    }
  };

  @Override public boolean canTip(final EnhancedForStatement x) {
    return anyTips(tippers, az.block(parent(x)))//
        && nonTips(rivals, x)//
    ;
  }

  @Override public Tip pattern(final EnhancedForStatement $) {
    return firstTip(tippers, az.block(parent($)));
  }

  @Override public Category category() {
    return Category.Iterative;
  }

  @Override public String description() {
    return "Iterate a collection for the first element matching some predicate";
  }

  @Override public String technicalName() {
    return "ReturnFirstInCSatisfyingXIfNoneY";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
