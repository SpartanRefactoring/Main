package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.Matcher.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class FindFirstBlock extends NanoPatternTipper<Block> {
  private static final List<UserDefinedTipper<Block>> tippers = new ArrayList<UserDefinedTipper<Block>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.statementsPattern("for($T $N : $X1) if($X2) return $N;", "return $X1.stream().findFirst($N -> $X2).get();",
          "Go Fluent : FindFirst", Option.LAST_IN_BLOCK));
      add(TipperFactory.statementsPattern("for($T $N : $X1) if($X2) return $N; throw $X3;",
          "if($X1.stream().anyMatch($N -> $X2)) return $X1.stream().findFirst($N -> $X2).get(); throw $X3;", "Go Fluent : FindFirst"));
      add(TipperFactory.statementsPattern("for($T $N : $X1) if($X2) {$N2 = $N; break;}", "$N2 = $X1.stream().findFirst($N -> $X2).get();",
          "Go Fluent : FindFirst"));
    }
  };

  @Override public String description(@SuppressWarnings("unused") final Block __) {
    return "";
  }

  @Override public boolean canTip(final Block x) {
    return anyTips(tippers, x);
  }

  @Override public Tip pattern(final Block x) {
    return firstTip(tippers, x);
  }
}
