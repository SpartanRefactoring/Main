package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

import java.util.List;

import org.eclipse.jdt.core.dom.WhileStatement;

import fluent.ly.as;
import il.org.spartan.spartanizer.research.UserDefinedTipper;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

/** like {@link CountIf but for while loops}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-29 */
public class While {
  public static class CountIf extends NanoPatternTipper<WhileStatement> {
    private static final long serialVersionUID = 0x15E0706E6496C76FL;
    private static final String description = "CountIf pattern: conevrt to fluent API";
    private static final List<UserDefinedTipper<WhileStatement>> tippers = as.list(
        patternTipper("while($X1) if($X2) ++$N3;", "$N3 += countWhile(()->$X1).If(()->$X2);", description),
        patternTipper("while($X1) ++$N3;", "$N3 += countWhile(()->$X1);", description));

    @Override public boolean canTip(final WhileStatement ¢) {
      return anyTips(tippers, ¢);
    }
    @Override public Tip pattern(final WhileStatement ¢) {
      return firstTip(tippers, ¢);
    }
    @Override public String description() {
      return "Count elements in collection that satisfy some predicate";
    }
    @Override public String tipperName() {
      return Aggregate.class.getSimpleName();
    }
  }
}
