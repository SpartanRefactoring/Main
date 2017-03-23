package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** like {@link CountIf but for while loops}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-29 */
public class While {
  public static class CountIf extends NanoPatternTipper<WhileStatement> {
    private static final long serialVersionUID = 1576383489015990127L;
    private static final String description = "CountIf pattern: conevrt to fluent API";
    private static final List<UserDefinedTipper<WhileStatement>> tippers = as.list(
        patternTipper("while($X1) if($X2) ++$N3;", "$N3 += countWhile(()->$X1).If(()->$X2);", description),
        patternTipper("while($X1) ++$N3;", "$N3 += countWhile(()->$X1);", description));

    @Override public boolean canTip(final WhileStatement ¢) {
      return anyTips(tippers, ¢);
    }

    @Override @Nullable public Tip pattern(final WhileStatement ¢) {
      return firstTip(tippers, ¢);
    }

    @Override @NotNull public String description() {
      return "Count elements in collection that satisfy some predicate";
    }

    @Override @NotNull public String nanoName() {
      return Aggregate.class.getSimpleName();
    }
  }
}
