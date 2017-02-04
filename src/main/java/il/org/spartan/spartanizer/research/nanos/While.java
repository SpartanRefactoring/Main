package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** like {@link CountIf but for while loops}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-29 */
public class While {
  public static class CountIf extends NanoPatternTipper<WhileStatement> {
    private static final String description = "CountIf pattern: conevrt to fluent API";
    private static final List<UserDefinedTipper<WhileStatement>> tippers = new ArrayList<UserDefinedTipper<WhileStatement>>() {
      static final long serialVersionUID = 1L;
      {
        add(patternTipper("while($X1) if($X2) ++$N3;", "$N3 += countWhile(()->$X1).If(()->$X2);", description));
        add(patternTipper("while($X1) ++$N3;", "$N3 += countWhile(()->$X1);", description));
      }
    };

    @Override public boolean canTip(final WhileStatement ¢) {
      return anyTips(tippers, ¢);
    }

    @Override @Nullable public Tip pattern(final WhileStatement ¢) {
      return firstTip(tippers, ¢);
    }

    @Override @NotNull public String description() {
      return "Count elements in collection that satisfy some predicate";
    }

    @Override @NotNull public String technicalName() {
      return "CountEInCSatisfyingX";
    }

    @Override public String example() {
      return firstPattern(tippers);
    }

    @Override public String symbolycReplacement() {
      return firstReplacement(tippers);
    }
  }
}
