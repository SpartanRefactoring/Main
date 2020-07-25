package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

import java.util.Collection;

import org.eclipse.jdt.core.dom.ConditionalExpression;

import fluent.ly.as;
import il.org.spartan.spartanizer.research.UserDefinedTipper;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

/** Min between two expressions
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-12 */
public final class Min extends NanoPatternTipper<ConditionalExpression> {
  private static final long serialVersionUID = 0x5485292F272BB07AL;
  private static final Collection<UserDefinedTipper<ConditionalExpression>> tippers = as.list(
      patternTipper("$X1 > $X2 ? $X2 : $X1", "min($X1,$X2)", "min"), patternTipper("$X2 < $X1 ? $X2 : $X1", "min($X1,$X2)", "min"),
      patternTipper("$X1 >= $X2 ? $X2 : $X1", "min($X1,$X2)", "min"), patternTipper("$X2 <= $X1 ? $X2 : $X1", "min($X1,$X2)", "min"));

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢);
  }
  @Override public Tip pattern(final ConditionalExpression ¢) {
    return firstTip(tippers, ¢);
  }
  @Override public String description() {
    return "Min between two expressions";
  }
}
